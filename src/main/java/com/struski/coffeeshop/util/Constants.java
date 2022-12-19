package com.struski.coffeeshop.util;

import java.util.Map;

public class Constants {

    public static final String COFFEE = "coffee";
    public static final String ROLL = "bacon roll";
    public static final String JUICE = "orange juice";
    public static final String EXTRA_MILK = "extra milk";
    public static final String FOAMED_MILK = "foamed milk";
    public static final String SPECIAL_ROAST = "special roast";
    public static final String WITH = " with ";
    public static final String LANGUAGE_TAG = "de-CH";
    public static final String RECEIPT_FOOTER_SEPARATOR = "\n--------------------\n";
    public static final String RECEIPT_COLUMNS = "%-2s %-40s %-22s";
    public static final String SEPARATOR = ",";
    public static final Integer DISCOUNT_XTH_BEVERAGE = 5;
    public static final Integer DISCOUNT_FREE_EXTRAS = 1;

    private static final Map<String, String> itemNames = Map.of(
            COFFEE, "Coffee",
            ROLL, "Bacon Roll",
            JUICE, "Freshly squeezed orange juice",
            EXTRA_MILK, "Extra milk",
            FOAMED_MILK, "Foamed milk",
            SPECIAL_ROAST, "Special roast coffee"
    );

    public static String getDisplayName(String key) {
        return itemNames.getOrDefault(key, key.substring(0, 1).toUpperCase() + key.substring(1));
    }
}
