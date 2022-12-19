package com.struski.coffeeshop.decorator;

import com.struski.coffeeshop.model.ItemType;
import com.struski.coffeeshop.model.Order;
import com.struski.coffeeshop.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.struski.coffeeshop.util.Constants.DISCOUNT_XTH_BEVERAGE;

public class EveryXthBeverageFree implements Discount, Function<Order, Order> {

    public void updateOrderItem(OrderItem orderItem) {
        if (orderItem.getChildren().isEmpty()) {
            orderItem.discount(orderItem.getDiscount().add(orderItem.getPrice()));
        } else {
            orderItem.discount(
                    orderItem.getDiscount().add(
                            orderItem.getChildren().stream()
                                    .filter(i -> ItemType.BEVERAGE.equals(i.getType()))
                                    .map(OrderItem::getPrice)
                                    .findFirst().orElse(BigDecimal.ZERO)));

        }
    }

    public Boolean isApplicable(Order order) {
        return order.getOrderItems().stream()
                .filter(i -> ItemType.BEVERAGE.equals(i.getType()))
                .count() >= DISCOUNT_XTH_BEVERAGE;
    }

    public Order apply(Order order) {
        if (!isApplicable(order)) {
            System.out.format("Discount %s is not applicable\n", this.getClass().getSimpleName());
            return order;
        }
        System.out.format("Discount %s will be applied\n", this.getClass().getSimpleName());
        List<OrderItem> beverages = order.getOrderItems().stream()
                .filter(i -> ItemType.BEVERAGE.equals(i.getType()))
                .sorted(((o1, o2) -> o1.getPrice().subtract(o2.getPrice()).intValue()))
                .collect(Collectors.toList());

        var discountedNum = beverages.size() / DISCOUNT_XTH_BEVERAGE;

        beverages.stream()
                .limit(discountedNum)
                .forEach(this::updateOrderItem);
        return order;
    }
}
