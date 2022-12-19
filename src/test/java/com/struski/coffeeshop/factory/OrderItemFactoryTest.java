package com.struski.coffeeshop.factory;

import com.struski.coffeeshop.model.Order;
import com.struski.coffeeshop.model.OrderItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderItemFactoryTest {

    private static OrderItemFactory factory;

    @BeforeAll
    static void beforeAll() {
        factory = new OrderItemFactory();
    }

    @ParameterizedTest(
            name = "[{index}] - {0} order items should be created of which {1} complex"
    )
    @CsvSource(value = {
            "4:2:large coffee with extra milk, small coffee with special roast, bacon roll, orange juice",
            "3:2:large coffee with extra milk, small coffee with special roast, orange juice",
            "4:2:medium coffee with extra milk, medium coffee with special roast, bacon roll, orange juice",
            "5:0:small coffee, medium coffee, large coffee, bacon roll, orange juice",
            "5:1:small coffee, small coffee, small coffee, small coffee, small coffee with foamed milk",
            "3:3:small coffee with extra milk, small coffee with extra milk , small coffee with special roast",
            "6:3:small coffee with extra milk, small coffee with extra milk , small coffee with special roast, bacon roll, bacon roll, orange juice",
    }, delimiter = ':')
    void create(String expectedAll, String expectedComplex, String input) {
        var order = new Order(input);
        List<OrderItem> items = order.getOrderItemsStr().stream()
                .map(factory::create)
                .collect(Collectors.toList());
        assertEquals(Integer.valueOf(expectedAll), items.size());
        var complexCount = (int) items.stream().filter(i -> !i.getChildren().isEmpty()).count();
        assertEquals(Integer.valueOf(expectedComplex), complexCount);
    }
}