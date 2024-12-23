package org.example;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String[] input = {"apple", "banana", "apple", "orange", "banana", "banana"};
        Map<String, Long> result = elementCounter(input);
        System.out.println(result);
    }

    public static <T> Map<T, Long> elementCounter(T[] array) {
        return Arrays.stream(array)
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }
}