package com.struski.coffeeshop.model;

import java.util.Objects;

public class ItemVariant {
    public static final Integer DEFAULT_ORDER = 1;
    public static final String DEFAULT_NAME = "";
    private final String name;
    private final Integer order;

    private ItemVariant(String name, Integer order) {
        this.name = name;
        this.order = order;
    }

    public static ItemVariant variantOf(String name) {
        return new ItemVariant(name, DEFAULT_ORDER);
    }

    public static ItemVariant variantOf(String name, Integer order) {
        return new ItemVariant(name, order);
    }

    public static ItemVariant defaultVariant() {
        return new ItemVariant(DEFAULT_NAME, DEFAULT_ORDER);
    }

    public String getName() {
        return name;
    }


    public Integer getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemVariant that = (ItemVariant) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
