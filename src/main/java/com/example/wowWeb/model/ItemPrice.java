package com.example.wowWeb.model;

import javax.persistence.*;

@Entity(name = "itemPrice")
@Table(name = "actual_recipes_profit")
public class ItemPrice {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Integer itemID;

    @Column(name = "recipe_id")
    private Integer recipeID;

    @Column(name = "name")
    private String name;

    @Column(name = "actual_recipe_price")
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
