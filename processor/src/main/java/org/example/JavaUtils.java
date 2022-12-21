package org.example;

/**
 * @author anhnsq@viettel.com.vn
 */
public class JavaUtils {
  private JavaUtils() {}

  public static String upCaseFirstCharacter(String raw) {
    if (raw == null || raw.isBlank()) return "";

    return raw.substring(0, 1).toUpperCase() + raw.substring(1);
  }
}
