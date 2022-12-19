package com.struski.coffeeshop.model;

public abstract class Item {
    protected String name;
    protected final ItemType type;

    public Item(ItemType type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }
}
