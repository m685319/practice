package org.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        var retval = filter(new String[]{"a", "bb", "ccc", "dddd"}, s -> s.concat("1"));
        System.out.println(Arrays.toString(retval));
    }

    public static <T> T[] filter(T[] array, Filter<T> filter) {
        T[] result = Arrays.copyOf(array, array.length);

        for (int i = 0; i < result.length; i++) {
            result[i] = filter.apply(array[i]);
        }
        return result;
    }
}

@FunctionalInterface
interface Filter<T> {
    T apply(T obj);
}