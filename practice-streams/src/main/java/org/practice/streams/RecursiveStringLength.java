package org.practice.streams;

import static java.util.Comparator.comparingInt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecursiveStringLength {

  public static void main(String[] args) throws IOException, URISyntaxException {

    Path path = Paths.get(FileExercise.class.getClassLoader().getResource("test.txt").toURI());
    Stream<String> linesRecursively = Files.lines(path);

    //longest line using recursion
    System.out.println(findLongestLine("", linesRecursively.collect(Collectors.toList()), 0));

    //longest line using Stream reduce
    Stream<String> linesReduced = Files.lines(path);
    System.out.println(linesReduced.reduce((x, y) -> {
      if (x.length() > y.length()) {
        return x;
      }
      return y;
    }).get());

    //longest line using Comparator
    Stream<String> linesUsingComparator = Files.lines(path);
    System.out.println(linesUsingComparator.max(comparingInt(String::length)).get());
  }

  private static String findLongestLine(String longest, List<String> lines, int index) {

    if (lines.get(index).length() > longest.length()) {
      longest = lines.get(index);
    }

    if (index < lines.size() - 1) {
      findLongestLine(longest, lines, ++index);
    }

    if (longest.length() > lines.get(index).length()) {
      return longest;
    }
    return lines.get(index);
  }
}
