package org.practice.streams;

import static java.util.Arrays.stream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SortStringArray {

  public static void main(String[] args) {
    final String[] stringArray = new String[]{"Rajesh", "Aryan", "Ammus"};
    List<String> sortedList = stream(stringArray).sorted().toList();

    //print the string array
    Stream.of(stringArray).sorted().forEach(System.out::println);
    //print the list
    sortedList.forEach(System.out::println);
  }
}
