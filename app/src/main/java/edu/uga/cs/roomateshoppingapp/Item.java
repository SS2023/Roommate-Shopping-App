package edu.uga.cs.roomateshoppingapp;

import java.io.Serializable;

public class Item implements Serializable {

    private String itemName;
    private String itemQuantity;
    private Double price;
    private String whoAdded;
    private String whoPurchased;

    public Item()
    {
        this.itemName = null;
        this.itemQuantity = null;
        this.price = null;
        this.whoAdded = null;
        this.whoPurchased = null;
    }

    public Item( String itemName, String itemQuantity, Double itemPrice, String whoAdded, String whoPurchased ) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.price = itemPrice;
        this.whoAdded = whoAdded;
        this.whoPurchased = whoPurchased;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getWhoAdded() {
        return whoAdded;
    }

    public void setWhoAdded(String whoAdded) {
        this.whoAdded = whoAdded;
    }

    public String getWhoPurchased() {
        return whoPurchased;
    }

    public void setWhoPurchased(String whoPurchased) {
        this.whoPurchased = whoPurchased;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", itemQuantity='" + itemQuantity + '\'' +
                ", price=" + price +
                ", whoAdded='" + whoAdded + '\'' +
                ", whoPurchased='" + whoPurchased + '\'' +
                '}';
    }
}
