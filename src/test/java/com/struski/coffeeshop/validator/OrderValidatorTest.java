package com.struski.coffeeshop.validator;

import com.struski.coffeeshop.model.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderValidatorTest {

    private static OrderValidator validator;

    @BeforeAll
    static void beforeAll() {
        validator = new OrderValidator();
    }

    @ParameterizedTest(
            name = "[{index}] - Order should have {0} invalid entries"
    )
    @CsvSource(value = {
            "0:large coffee with extra milk, small coffee with special roast, bacon roll, orange juice",
            "0:medium coffee with extra milk, medium coffee with special roast, bacon roll, orange juice",
            "0:small coffee, medium coffee, large coffee, bacon roll, orange juice",
            "1:test, test, bacon roll, orange juice",
            "2:test1, test2, bacon roll, orange juice",
            "2:extra milk, special roast, bacon roll, orange juice",
            "2:orange juice with extra milk, orange juice with special roast, bacon roll",
            "2:bacon roll with extra milk, bacon roll with special roast, bacon roll",
            "2:bacon roll with orange juice, bacon roll with large coffee, bacon roll",
    }, delimiter = ':')
    void getInvalidOrderItems(String expected, String input) {
        var order = new Order(input);
        var invalidItems = validator.getInvalidOrderItems(order);
        assertNotNull(invalidItems);
        assertEquals(Integer.valueOf(expected), invalidItems.size());
    }


}