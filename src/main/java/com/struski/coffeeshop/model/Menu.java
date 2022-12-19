package com.struski.coffeeshop.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.struski.coffeeshop.model.ItemType.*;
import static com.struski.coffeeshop.model.ItemVariant.defaultVariant;
import static com.struski.coffeeshop.model.ItemVariant.variantOf;
import static com.struski.coffeeshop.util.Constants.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class Menu {

    private static Menu INSTANCE;

    private String languageTag;

    Set<MenuItem> items = new LinkedHashSet<>();

    Map<String, OrderItem> orderItems = new HashMap<>();

    public Predicate<String> isExtraAllowed = s -> s.contains(COFFEE);

    private Menu() {
    }

    public static String currencyFormat(BigDecimal n, String languageTag) {
        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag(languageTag)).format(n);
    }

    private void init() {
        getInstance()
                .languageTag(LANGUAGE_TAG)
                .item(new MenuItem(
                        BEVERAGE, COFFEE,
                        Map.of(variantOf("small", 1), new BigDecimal("2.50"),
                                variantOf("medium", 2), new BigDecimal("3.00"),
                                variantOf("large", 3), new BigDecimal("3.50"))))
                .item(new MenuItem(BEVERAGE, JUICE, Map.of(variantOf("0.25l", 1), new BigDecimal("3.95"))))
                .item(new MenuItem(SNACK, ROLL, Map.of(defaultVariant(), new BigDecimal("4.50"))))
                .item(new MenuItem(EXTRA, EXTRA_MILK, Map.of(defaultVariant(), new BigDecimal("0.30"))))
                .item(new MenuItem(EXTRA, FOAMED_MILK, Map.of(defaultVariant(), new BigDecimal("0.50"))))
                .item(new MenuItem(EXTRA, SPECIAL_ROAST, Map.of(defaultVariant(), new BigDecimal("0.50"))));

    }


    public static Menu getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Menu();
            INSTANCE.init();
        }

        return INSTANCE;
    }

    public Menu item(MenuItem item) {
        items.add(item);
        INSTANCE.addAvailableItems(item);
        return INSTANCE;
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public Menu languageTag(String languageTag) {
        this.languageTag = languageTag;
        return INSTANCE;
    }

    private void addAvailableItems(MenuItem item) {
        item.getVariantToPrice()
                .entrySet()
                .stream()
                .map(e -> getVariantOrderItem(item, e))
                .forEach(i -> orderItems.put(i.getName(), i));
    }

    private OrderItem getVariantOrderItem(MenuItem item, Map.Entry<ItemVariant, BigDecimal> e) {
        // if there is only one variant of an item then
        // client does not need to specify which one he wants
        var variant = "";
        if (item.getVariantToPrice().size() > 1) {
            variant = e.getKey().getName();
        }
        return new OrderItem(
                item.getType(),
                String.format("%s %s", variant, item.getName()).trim(),
                e.getValue()
        );
    }

    /**
     * Checks if string passed matches any of the menu items
     *
     * @param itemStr
     * @return
     */
    public Boolean isOnTheMenu(String itemStr) {
        return orderItems.containsKey(itemStr);
    }

    public OrderItem getOrderItem(String key) {
        return orderItems.get(key);
    }

    @Override
    public String toString() {
        var groupedMenu = items.stream()
                .collect(groupingBy(MenuItem::getType, LinkedHashMap::new, Collectors.toList()));

        String beverages = groupedMenu.get(BEVERAGE).stream()
                .map(item -> item.getMenuEntry(getLanguageTag()))
                .sorted()
                .collect(joining("\n"));

        String snacks = groupedMenu.get(SNACK).stream()
                .map(item -> item.getMenuEntry(getLanguageTag()))
                .sorted()
                .collect(joining("\n"));

        String extras = groupedMenu.get(EXTRA).stream()
                .map(item -> item.getMenuEntry(getLanguageTag()))
                .sorted()
                .collect(joining("\n"));

        return String.format("Menu:\n\n%s\n%s\n\nExtras:\n%s\n", beverages, snacks, extras);
    }
}
