package org.example;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author anhnsq@viettel.com.vn
 */
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("org.example.Property")
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {
  private Types typeUtils;
  private Elements elementUtils;
  private Filer filer;
  private Messager messager;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    typeUtils = processingEnv.getTypeUtils();
    elementUtils = processingEnv.getElementUtils();
    filer = processingEnv.getFiler();
    messager = processingEnv.getMessager();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<? extends Element> elementsAnnotatedWithProperty = roundEnv.getElementsAnnotatedWith(Property.class);

    Map<Boolean, List<Element>> annotatedElementsMap = elementsAnnotatedWithProperty.stream().collect(
      Collectors.partitioningBy(element -> element.getKind() == ElementKind.FIELD)
    );
    List<Element> validElements = annotatedElementsMap.get(true);
    List<Element> invalidElements = annotatedElementsMap.get(false);

    invalidElements.forEach(e ->
      messager.printMessage(Diagnostic.Kind.ERROR,
        "@Property must be applied to a property only, found", e));

    if (validElements.isEmpty()) {
      return true;
    }

    Map<String, List<Element>> mapElementWithClassName = new HashMap<>();
    validElements.forEach(e -> {
      String className = ((TypeElement) e.getEnclosingElement()).getQualifiedName().toString();
      mapElementWithClassName.computeIfAbsent(className, k -> new ArrayList<>()).add(e);
    });
    try {
      for (Map.Entry<String, List<Element>> entry : mapElementWithClassName.entrySet()) {
        processAnnotationForClass(entry.getKey(), entry.getValue());
      }
    } catch (IOException e) {
      messager.printMessage(Diagnostic.Kind.ERROR, "IOException");
    } catch (ClassNotFoundException e) {
      messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
    }

    return false;
  }

  private void processAnnotationForClass(String className, List<Element> properties) throws IOException, ClassNotFoundException {
    String packageName = null;
    int lastDot = className.lastIndexOf('.');
    if (lastDot > 0) {
      packageName = className.substring(0, lastDot);
    }
    String builderClassName = className.substring(lastDot + 1) + "Builder";
    String objectName = properties.get(0).getEnclosingElement().getSimpleName().toString();

    JavaFileObject builderFile = filer.createSourceFile(className + "Builder");
    try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
      if (packageName != null) {
        out.print("package ");
        out.print(packageName);
        out.println(";");
        out.println();
      }

      out.print("public class ");
      out.print(builderClassName);
      out.println(" {");
      out.println();

      out.print("    private ");
      out.print(objectName);
      out.print(" instance = new ");
      out.print(objectName);
      out.println("();");
      out.println();

      out.print("    public ");
      out.print(objectName);
      out.println(" build() {");
      out.println("        return instance;");
      out.println("    }");
      out.println();

      for (Element property : properties) {
        String propertyName = property.getSimpleName().toString();
        String propertyType = property.asType().toString();
        out.println(String.format("    public %s %s(%s %s) {", builderClassName, "set" +
          JavaUtils.upCaseFirstCharacter(propertyName), propertyType, propertyName));
        out.println(String.format("      this.instance.set%s(%s);", JavaUtils.upCaseFirstCharacter(propertyName), propertyName));
        out.println("      return this;");
        out.println("    }");
      }

      out.println("}");
    }
  }
}
