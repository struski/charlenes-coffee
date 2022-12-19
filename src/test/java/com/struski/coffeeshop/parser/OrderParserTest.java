package com.struski.coffeeshop.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderParserTest {
    @ParameterizedTest(
            name = "[{index}] - Parse order with {0} entries"
    )
    @CsvSource(value = {
            "4:large coffee with extra milk, small coffee with special roast, bacon roll, orange juice",
            "4:extra milk, , , , special roast,bacon roll, , , orange juice",
            "6:,,,test,,,,,,,test,,,,,large coffee, test, bacon roll, orange juice,,,,,,,,,"
    }, delimiter = ':')
    void parseSuccess(String expected, String input) {
        List<String> parsed = OrderParser.parse(input);
        assertNotNull(parsed);
        assertEquals(Integer.valueOf(expected), parsed.size());
    }
}