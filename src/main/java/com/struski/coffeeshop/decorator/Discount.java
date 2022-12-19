package com.struski.coffeeshop.decorator;

import com.struski.coffeeshop.model.Order;
import com.struski.coffeeshop.model.OrderItem;

public interface Discount {

    /**
     * Updates one of the items in the Order
     * @param orderItem
     */
    void updateOrderItem(OrderItem orderItem);

    /**
     * Checks if discount is applicable
     * @param order
     * @return - true if discount can be applied
     */
    Boolean isApplicable(Order order);
}
