package com.struski.coffeeshop.model;

import com.struski.coffeeshop.util.Constants;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class MenuItem extends Item {

    private final Map<ItemVariant, BigDecimal> variantToPrice;

    private final String displayName;

    public MenuItem(ItemType type, String name, Map<ItemVariant, BigDecimal> variantToPrice) {
        super(type, name);
        if (variantToPrice.isEmpty()) {
            throw new IllegalStateException("MenuItems must have at least one variant");
        }
        this.variantToPrice = variantToPrice;
        this.displayName = Constants.getDisplayName(name);
    }

    public Map<ItemVariant, BigDecimal> getVariantToPrice() {
        return variantToPrice;
    }

    private String getDisplayName() {
        return displayName;
    }

    public String getMenuEntry(String languageTag) {
        return new StringBuilder()
                .append(getDisplayName())
                .append(variantToPrice.entrySet().stream()
                        .sorted(Comparator.comparingInt(e -> e.getKey().getOrder()))
                        .map(Map.Entry::getKey)
                        .map(ItemVariant::toString)
                        .filter(v -> !v.isBlank())
                        .collect(joining(", ", " (", ") "))
                        .replace(" () ", "")
                )
                .append(variantToPrice.entrySet().stream()
                        .sorted(Comparator.comparingInt(e -> e.getKey().getOrder()))
                        .map(Map.Entry::getValue)
                        .map(p -> Menu.currencyFormat(p, languageTag))
                        .collect(joining(", ", " ", "")))
                .toString();

    }

//    private String toOrderItemString(ItemVariant variant) {
//        return String.format("%s %s", variant, getName()).trim();
//    }
//
//    public OrderItem toOrderItem(ItemVariant variant) {
//        if (!variantToPrice.containsKey(variant)) {
//            String msg = String.format("Could not find variant %s for item %s", variant, getName());
//            throw new IllegalStateException(msg);
//        }
//        return new OrderItem(this.getType(), toOrderItemString(variant))
//                .price(variantToPrice.get(variant));
//    }

}
