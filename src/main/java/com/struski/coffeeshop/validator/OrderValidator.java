package com.struski.coffeeshop.validator;

import com.struski.coffeeshop.model.ItemType;
import com.struski.coffeeshop.model.Menu;
import com.struski.coffeeshop.model.Order;
import com.struski.coffeeshop.util.Constants;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderValidator implements Validator {

    public Boolean isValidItem(String itemStr) {
        return Menu.getInstance().isOnTheMenu(itemStr);
    }

    public Set<String> getInvalidOrderItems(Order order) {
        var notInMenu = order.getOrderItemsStr().stream()
                .flatMap(i -> Arrays.stream(i.split(Constants.WITH)))
                .filter(i -> !i.isBlank())
                .filter(i -> !isValidItem(i))
                .collect(Collectors.toSet());

        var extrasWithoutParent = order.getOrderItemsStr().stream()
                .filter(i -> !i.isBlank())
                .filter(i -> !i.contains(Constants.WITH))
                // filter underneath can be adjusted to work with multiple extras
                .filter(i -> notInMenu.stream().noneMatch(nimItem -> i.startsWith(nimItem) || i.endsWith(nimItem)))
                .filter(i -> ItemType.EXTRA.equals(Menu.getInstance().getOrderItem(i).getType()))
                .collect(Collectors.toSet());

        var invalidCombinationsWith = order.getOrderItemsStr().stream()
                .filter(i -> i.contains(Constants.WITH))
                // filter underneath can be adjusted to work with multiple extras
                .filter(i -> notInMenu.stream().noneMatch(nimItem -> i.startsWith(nimItem) || i.endsWith(nimItem)))
                .map(i -> i.split(Constants.WITH))
                // could be modified to work with collections instead of arrays
                .filter(arr -> arr.length > 1)
                .filter(arr -> Menu.getInstance().isExtraAllowed.negate().test(arr[0])
                        || Arrays.stream(arr)
                        .skip(1)
                        .anyMatch(itemStr -> !ItemType.EXTRA.equals(Menu.getInstance().getOrderItem(itemStr).getType())))
                .map(arr -> String.join(Constants.WITH, arr))
                .collect(Collectors.toSet());

        return Stream.concat(Stream.concat(notInMenu.stream(), extrasWithoutParent.stream()), invalidCombinationsWith.stream())
                .collect(Collectors.toSet());
    }

}
