package org.practice.streams;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

public class FileExercise {

  public static void main(String[] args) throws URISyntaxException, IOException {

    Path path = Paths.get(FileExercise.class.getClassLoader().getResource("test.txt").toURI());
    Stream<String> lines = Files.lines(path);

    //print the sorted lines based on line length
    lines.sorted(Comparator.comparingInt(String::length)).forEach(System.out::println);
    lines.close();
  }
}
