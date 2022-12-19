package com.struski.coffeeshop.model;

import com.struski.coffeeshop.parser.OrderParser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.struski.coffeeshop.util.Constants.*;
import static java.util.stream.Collectors.groupingBy;

public class Order {

    private final List<String> orderItemsStr;
    private final List<OrderItem> orderItems = new ArrayList<>();

    public Order(String orderStr) {
        this.orderItemsStr = OrderParser.parse(orderStr);
    }

    public List<String> getOrderItemsStr() {
        return orderItemsStr;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Order orderItem(OrderItem item) {
        orderItems.add(item);
        return this;
    }

    @Override
    public String toString() {
        return orderItems.stream()
                .collect(groupingBy(OrderItem::getName))
                .entrySet()
                .stream()
                .map(this::formatOrderItem)
                .collect(Collectors.joining("\n", "", formatReceiptFooter()));

    }

    private String formatOrderItem(Map.Entry<String, List<OrderItem>> e) {
        return new StringBuilder()
                .append(String.format(RECEIPT_COLUMNS + "\n",
                        e.getValue().size() + " X",
                        e.getKey(),
                        formatSum(e.getValue(), LANGUAGE_TAG)))
                .append(String.format(RECEIPT_COLUMNS, "", "", formatDiscount(e.getValue(), LANGUAGE_TAG)))
                .toString();
    }

    private String formatSum(List<OrderItem> items, String languageTag) {
        return Menu.currencyFormat(
                items.stream()
                        .map(OrderItem::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add), languageTag);
    }

    private String formatDiscount(List<OrderItem> items, String languageTag) {
        BigDecimal discount = items.stream()
                .map(OrderItem::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (BigDecimal.ZERO.equals(discount)) {
            return "";
        }
        return String.format("-%s", Menu.currencyFormat(discount, languageTag));
    }

    private String formatTotal(String languageTag) {
        return Menu.currencyFormat(getTotal(), languageTag);
    }

    public BigDecimal getTotal() {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String formatTotalDiscount(String languageTag) {
        return Menu.currencyFormat(getTotalDiscount(), languageTag);
    }

    public BigDecimal getTotalDiscount() {
        return orderItems.stream()
                .map(OrderItem::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String formatTotalWithDiscount(String languageTag) {
        return Menu.currencyFormat(orderItems.stream()
                .map(i -> i.getPrice().subtract(i.getDiscount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add), languageTag);
    }

    private String formatReceiptFooter() {
        return new StringBuilder()
                .append(String.format(RECEIPT_FOOTER_SEPARATOR))
                .append(String.format(RECEIPT_COLUMNS + "\n", "", "-(" + formatTotalDiscount(LANGUAGE_TAG) + ")", formatTotal(LANGUAGE_TAG)))
                .append(String.format(RECEIPT_COLUMNS + "\n", "", "Total:", formatTotalWithDiscount(LANGUAGE_TAG)))
                .toString();

    }
}
