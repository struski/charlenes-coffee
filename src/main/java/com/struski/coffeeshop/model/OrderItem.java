package com.struski.coffeeshop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderItem extends Item {

    protected BigDecimal price = BigDecimal.ZERO;

    protected BigDecimal discount = BigDecimal.ZERO;

    protected List<OrderItem> children = new ArrayList<>();

    public OrderItem(ItemType type, String name) {
        super(type, name);
    }

    public OrderItem(ItemType type, String name, BigDecimal price) {
        super(type, name);
        this.price = price;
    }

    public OrderItem(OrderItem existing) {
        super(existing.getType(), existing.getName());
        this.price = existing.getPrice();
        this.discount = existing.getDiscount();
        this.children = existing.getChildren();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderItem price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public OrderItem discount(BigDecimal discount) {
        this.discount = discount;
        return this;
    }

    public List<OrderItem> getChildren() {
        return children;
    }

    public OrderItem child(OrderItem child) {
        children.add(child);
        return price(children.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
