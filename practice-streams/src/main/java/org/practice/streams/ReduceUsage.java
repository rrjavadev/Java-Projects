package org.practice.streams;

import java.util.stream.Stream;

public class ReduceUsage {

  public static void main(String[] args) {
    Double total = Stream.of(1.2, 2.3, 4.5).reduce(0.0, Double::sum);
    System.out.println(total);
  }
}
