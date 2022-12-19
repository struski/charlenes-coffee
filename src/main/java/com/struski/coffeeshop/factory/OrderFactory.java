package com.struski.coffeeshop.factory;

import com.struski.coffeeshop.model.Order;
import com.struski.coffeeshop.validator.OrderValidator;

import java.util.function.Function;
import java.util.stream.Stream;

public class OrderFactory implements Factory<Order> {

    private static OrderFactory INSTANCE;

    private final OrderValidator validator;
    private final OrderItemFactory itemsFactory;

    private OrderFactory() {
        this.validator = new OrderValidator();
        this.itemsFactory = new OrderItemFactory();
    }

    public static OrderFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OrderFactory();
        }

        return INSTANCE;
    }

    @Override
    public Order create(String itemsStr) {
        var order = new Order(itemsStr);
        var invalidOrderItems = validator.getInvalidOrderItems(order);
        if (!invalidOrderItems.isEmpty()) {
            throw new IllegalArgumentException("Invalid order items found: " + invalidOrderItems);
        }

        order.getOrderItemsStr().stream()
                .map(itemsFactory::create)
                .forEach(order::orderItem);

        return order;
    }

    /**
     * This version of the function can be decorated with custom
     * behavior during runtime
     *
     * @param itemsStr
     * @param discounts
     * @return
     */
    public Order create(String itemsStr, Function<Order, Order>... discounts) {
        return Stream.of(discounts)
                .reduce(Function.identity(), Function::andThen)
                .apply(create(itemsStr));

    }
}
