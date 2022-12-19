package com.struski.coffeeshop.factory;

import com.struski.coffeeshop.decorator.BeverageSnackExtraFree;
import com.struski.coffeeshop.decorator.EveryXthBeverageFree;
import com.struski.coffeeshop.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderFactoryTest {

    @ParameterizedTest(
            name = "[{index}] - {0} order items should be created. Total price should be {1}"
    )
    @CsvSource(value = {
            "4:15.25:large coffee with extra milk, small coffee with special roast, bacon roll, orange juice",
            "3:10.75:large coffee with extra milk, small coffee with special roast, orange juice",
            "4:15.25:medium coffee with extra milk, medium coffee with special roast, bacon roll, orange juice",
            "5:17.45:small coffee, medium coffee, large coffee, bacon roll, orange juice",
            "5:13.00:small coffee, small coffee, small coffee, small coffee, small coffee with foamed milk",
            "3:8.60:small coffee with extra milk, small coffee with extra milk , small coffee with special roast",
            "6:21.55:small coffee with extra milk, small coffee with extra milk , small coffee with special roast, bacon roll, bacon roll, orange juice",
    }, delimiter = ':')
    void create(String expected, String expectedTotal, String input) {
        Order order = OrderFactory.getInstance().create(input);
        assertEquals(Integer.valueOf(expected), order.getOrderItemsStr().size());
        assertEquals(new BigDecimal(expectedTotal), order.getTotal());
    }

    @ParameterizedTest(
            name = "[{index}] - {0} order items should be created. Total price should be {1}"
    )
    @CsvSource(value = {
            "4:0.30:large coffee with extra milk, small coffee with special roast, bacon roll, orange juice",
            "3:0:large coffee with extra milk, small coffee with special roast, orange juice",
            "4:0.30:medium coffee with extra milk, medium coffee with special roast, bacon roll, orange juice",
            "5:0:small coffee, medium coffee, large coffee, bacon roll, orange juice",
            "5:2.50:small coffee, small coffee, small coffee, small coffee, small coffee with foamed milk",
            "3:0:small coffee with extra milk, small coffee with extra milk , small coffee with special roast",
            "6:0.30:small coffee with extra milk, small coffee with extra milk , small coffee with special roast, bacon roll, bacon roll, orange juice",
            "13:5.30:small coffee with extra milk, small coffee with extra milk , small coffee with special roast, bacon roll, orange juice, small coffee, small coffee, small coffee, small coffee,small coffee, small coffee, small coffee, small coffee",

    }, delimiter = ':')
    void createWithDiscounts(String expected, String expectedDiscount, String input) {
        Order order = OrderFactory.getInstance().create(input, new EveryXthBeverageFree(), new BeverageSnackExtraFree());
        assertEquals(Integer.valueOf(expected), order.getOrderItemsStr().size());
        assertEquals(new BigDecimal(expectedDiscount), order.getTotalDiscount());
    }


    @Test
    void getInstance() {
        var factory1 = OrderFactory.getInstance();
        var factory2 = OrderFactory.getInstance();
        assertNotNull(factory1);
        assertNotNull(factory2);
        assertSame(factory1, factory2);
    }
}