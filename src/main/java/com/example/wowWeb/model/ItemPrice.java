package com.example.wowWeb.model;

import javax.persistence.*;

public class ItemPrice {

    private Integer itemID;
    private Integer recipeID;
    private String name;
    private long lastAuctionPrice;

    public ItemPrice(Integer itemID, Integer recipeID, String name, long lastAuctionPrice) {
        this.itemID = itemID;
        this.recipeID = recipeID;
        this.name = name;
        this.lastAuctionPrice = lastAuctionPrice;
    }
    public ItemPrice(){
        super();
    }

    public Integer getItemID() {
        return itemID;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public long getLastAuctionPrice() {
        return lastAuctionPrice;
    }

    public String getName() {
        return name;
    }
}
