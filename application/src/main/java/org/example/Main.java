package org.example;

import org.example.model.Movie;
import org.example.model.MovieBuilder;

/**
 * @author anhnsq@viettel.com.vn
 */
public class Main {
  public static void main(String[] args) {
//    BookBuilder builder = new org.example.BookBuilder();
//    Book b = builder.setName("Harry Potter").setAuthor("J.K.Rowling").build();
//    System.out.println(b);
    MovieBuilder builder = new MovieBuilder();
    Movie mv = builder.setName("Harry Maguire").setRating(1.2).build();
    System.out.println(mv);
  }
}
