package com.mordeninaf.boot.firin.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberUtils {
    public static List<Integer> getTotalPages(int numberOfPages) {
        return IntStream.rangeClosed(0, numberOfPages-1).boxed().collect(Collectors.toList());
    }
}
