package org.example.model;

import org.example.Property;

/**
 * @author anhnsq@viettel.com.vn
 */
public class Movie {
  @Property
  private String name;

  @Property
  private double rating;

  @Override
  public String toString() {
    return "Movie{" +
      "name='" + name + '\'' +
      ", rating=" + rating +
      '}';
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }
}
