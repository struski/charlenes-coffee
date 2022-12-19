package com.struski.coffeeshop.parser;

import com.struski.coffeeshop.util.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderParser {

    private OrderParser() {
    }

    /**
     * Parses an order string and returns list of strings, each containing one item.
     * Each item string can contain information about extras.
     *
     * @param orderStr
     * @return
     */
    public static List<String> parse(String orderStr) {
        return Arrays.stream(orderStr.split(Constants.SEPARATOR))
                .map(itemStr -> itemStr.trim().replaceAll("\\s+", " "))
                .filter(itemStr -> !itemStr.isBlank())
                .collect(Collectors.toList());
    }
}
