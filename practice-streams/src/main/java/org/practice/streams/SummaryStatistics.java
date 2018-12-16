package org.practice.streams;

import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;

public class SummaryStatistics {

  public static void main(String[] args) {

    IntSummaryStatistics summaryStatistics = IntStream.of(1, 2, 3, 4, 5).summaryStatistics();
    System.out.print(summaryStatistics);
  }
}
