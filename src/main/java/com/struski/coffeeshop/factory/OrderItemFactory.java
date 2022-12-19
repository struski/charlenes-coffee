package com.struski.coffeeshop.factory;

import com.struski.coffeeshop.model.Menu;
import com.struski.coffeeshop.model.OrderItem;
import com.struski.coffeeshop.util.Constants;

import java.util.Arrays;
import java.util.function.Predicate;

public class OrderItemFactory implements Factory<OrderItem> {

    Predicate<String> isComplexItem = s -> s.contains(Constants.WITH);

    @Override
    public OrderItem create(String itemStr) {
        if (isComplexItem.negate().test(itemStr)) {
            return new OrderItem(Menu.getInstance().getOrderItem(itemStr));
        }
        String[] itemsArr = itemStr.split(Constants.WITH);
        var parentItemTemplate = Menu.getInstance().getOrderItem(itemsArr[0]);
        var parentItem = new OrderItem(parentItemTemplate.getType(), itemStr);

        Arrays.stream(itemsArr)
                .map(i -> new OrderItem(Menu.getInstance().getOrderItem(i)))
                .forEach(parentItem::child);

        return parentItem;
    }

}
