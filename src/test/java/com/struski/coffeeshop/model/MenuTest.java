package com.struski.coffeeshop.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @ParameterizedTest(
            name = "[{index}] - [{1}] should be on the menu"
    )
    @CsvSource(value = {
            "true:small coffee",
            "true:medium coffee",
            "true:large coffee",
            "true:extra milk",
            "true:foamed milk",
            "true:special roast",
            "true:orange juice",
            "true:bacon roll"
    }, delimiter = ':')
    void isOnTheMenu(String expected, String input) {
        assertEquals(Boolean.valueOf(expected), Menu.getInstance().isOnTheMenu(input));
    }

    @Test
    void getInstance() {
        var menu1 = Menu.getInstance();
        var menu2 = Menu.getInstance();
        assertNotNull(menu1);
        assertNotNull(menu2);
        assertSame(menu1, menu2);
    }
}