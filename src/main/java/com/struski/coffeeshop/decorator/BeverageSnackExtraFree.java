package com.struski.coffeeshop.decorator;

import com.struski.coffeeshop.model.ItemType;
import com.struski.coffeeshop.model.Order;
import com.struski.coffeeshop.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.struski.coffeeshop.util.Constants.DISCOUNT_FREE_EXTRAS;
import static java.util.stream.Collectors.groupingBy;

public class BeverageSnackExtraFree implements Discount, Function<Order, Order> {

    public void updateOrderItem(OrderItem orderItem) {

        BigDecimal firstExtraPrice = orderItem.getChildren().stream()
                .filter(i -> ItemType.EXTRA.equals(i.getType()))
                .map(OrderItem::getPrice)
                .findFirst().orElse(BigDecimal.ZERO);

        orderItem.discount(orderItem.getDiscount().add(firstExtraPrice));

    }

    public Boolean isApplicable(Order order) {
        var bevSnackCounts = order.getOrderItems().stream()
                .filter(i -> ItemType.BEVERAGE.equals(i.getType()) || ItemType.SNACK.equals(i.getType()))
                .collect(groupingBy(OrderItem::getType, Collectors.counting()));
        return bevSnackCounts.keySet().size() == 2 && bevSnackCounts.values().stream().allMatch(c -> c >= 1);
    }

    @Override
    public Order apply(Order order) {
        if (!isApplicable(order)) {
            System.out.format("Discount %s is not applicable\n", this.getClass().getSimpleName());
            return order;
        }
        System.out.format("Discount %s will be applied\n", this.getClass().getSimpleName());
        List<OrderItem> extras = order.getOrderItems().stream()
                .filter(i -> !i.getChildren().isEmpty())
                .filter(i -> i.getChildren().stream().anyMatch(c -> ItemType.EXTRA.equals(c.getType())))
                .sorted(((o1, o2) -> o1.getPrice().subtract(o2.getPrice()).intValue()))
                .collect(Collectors.toList());

        extras.stream()
                .limit(DISCOUNT_FREE_EXTRAS)
                .forEach(this::updateOrderItem);

        return order;
    }
}
