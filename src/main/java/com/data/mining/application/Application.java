package com.data.mining.application;

import java.util.List;

import com.data.mining.item.Item;

/**
 * Represent application that consolidates {@link Item}
 */
public class Application {

    /**
     * List of all items within this application.
     */
    public List<Item> items;

    public Application(List<Item> items) {
        this.items = items;
    }


}
