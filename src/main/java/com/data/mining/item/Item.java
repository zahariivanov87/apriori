package com.data.mining.item;

/**
 *  Item that is used for classification.
 */
public class Item {

    /**
     * Value of the item that will be used for calculation of support and confidence.
     */
    public String value;

    /**
     * Wrapper of the object that is associated with current Item.
     */
    public Object item;

    public Item(String value) {
        this.value = value;
    }

    public Item(String value, Object item) {
        this.value = value;
        this.item = item;
    }
}
