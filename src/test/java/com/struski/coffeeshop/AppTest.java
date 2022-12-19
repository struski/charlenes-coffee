package com.struski.coffeeshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    @Test
    public void mainWithoutParameters() {
        var parameters = new String[]{};
        App.main(parameters);
    }

    @ParameterizedTest(
            name = "[{index}] - main method should run with parameters: {0}"
    )
    @CsvSource(value = {
            "large coffee with extra milk, small coffee with special roast, bacon roll, orange juice",
            "large coffee with extra milk, small coffee with special roast, orange juice",
            "medium coffee with extra milk, medium coffee with special roast, bacon roll, orange juice",
            "small coffee, medium coffee, large coffee, bacon roll, orange juice",
            "small coffee, small coffee, small coffee, small coffee, small coffee, bacon roll, orange juice",
            "small coffee, small coffee, small coffee, small coffee, small coffee with foamed milk, bacon roll, orange juice",
            "small coffee with extra milk, small coffee with extra milk , small coffee with special roast, bacon roll, orange juice",
            "small coffee with extra milk, small coffee with extra milk , small coffee with special roast, bacon roll, orange juice",
            "small coffee with extra milk, small coffee with extra milk , small coffee with special roast, bacon roll, orange juice, small coffee, small coffee, small coffee, small coffee,small coffee, small coffee, small coffee, small coffee",
    }, delimiter = ':')
    public void mainWitParametersSuccess(String input) {
        var parameters = new String[]{input};
        App.main(parameters);
    }

    @ParameterizedTest(
            name = "[{index}] - main method should not run with parameters: {0}"
    )
    @CsvSource(value = {
            "extra milk, special roast, bacon roll, orange juice",
            "test, test, extra milk, special roast, bacon roll, orange juice",
            "test1, test2, extra milk, special roast, bacon roll, orange juice"
    }, delimiter = ':')
    public void mainWitParametersFail(String input) {
        var parameters = new String[]{input};

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            App.main(parameters);
        });

        String expectedMessage = "Invalid order items found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}