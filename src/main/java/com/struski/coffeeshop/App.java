package com.struski.coffeeshop;

import com.struski.coffeeshop.decorator.BeverageSnackExtraFree;
import com.struski.coffeeshop.decorator.EveryXthBeverageFree;
import com.struski.coffeeshop.factory.OrderFactory;
import com.struski.coffeeshop.model.Menu;

import java.util.Arrays;

public class App {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(Menu.getInstance());
            return;
        }
        System.out.format("\nYour orders:\n");
        var orders = Arrays.asList(args);
        System.out.println(orders);
        if (args.length > 1) {
            System.out.println("Multiple orders are not yet supported. Only the first order will be processed");
        }

        System.out.format("\nReceipt:\n%s", OrderFactory.getInstance()
                .create(orders.get(0),
                        new EveryXthBeverageFree(),
                        new BeverageSnackExtraFree())
                .toString());
    }
}
