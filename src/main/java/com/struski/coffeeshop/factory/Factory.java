package com.struski.coffeeshop.factory;

public interface Factory<T> {
    T create(String s);
}
