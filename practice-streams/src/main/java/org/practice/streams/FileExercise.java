package org.practice.streams;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileExercise {

  public static void main(String[] args) throws URISyntaxException, IOException {

    Path path = Paths.get(FileExercise.class.getClassLoader().getResource("test.txt").toURI());

    Stream<String> maxLineLength = Files.lines(path);

    //length of longest line.
    System.out.println(maxLineLength.max(comparing(String::length)).get().length());
    maxLineLength.close();

    //print the sorted lines based on line length
    Stream<String> sortedLines = Files.lines(path);
    sortedLines.sorted(comparingInt(String::length)).forEach(System.out::println);

    //Longest line
    Stream<String> longestLine = Files.lines(path);
    System.out.println(longestLine.max(comparing(String::length)).get());
  }
}
