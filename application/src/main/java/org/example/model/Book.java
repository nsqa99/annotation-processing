package org.example.model;

import org.example.Property;

/**
 * @author anhnsq@viettel.com.vn
 */
public class Book {
  @Property
  private String name;
  @Property
  private String author;
  @Property
  private int publishYear;

  public void setName(String name) {
    this.name = name;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setPublishYear(int publishYear) {
    this.publishYear = publishYear;
  }

  @Override
  public String toString() {
    return "Book{" +
      "name='" + name + '\'' +
      ", author='" + author + '\'' +
      ", publishYear=" + publishYear +
      '}';
  }
}
