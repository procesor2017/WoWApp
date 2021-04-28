package com.example.wowWeb.model;

import javax.persistence.*;

@Entity(name = "item")
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "items_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private Integer image;

    @Column(name = "buy_price")
    private Integer purchasePrice = 0;

    @Column(name = "sell_price")
    private Integer sellPrice = 0;

    @Column(name = "class")
    private Integer item_class = 0;

    @Column(name = "subclass")
    private Integer item_subclass = 0;

    public Item(String name, int id, int image, int purchasePrice, int sellPrice, int item_class, int item_subclass){
        this.name = name;
        this.id = id;
        this.image = image;
        this.purchasePrice = purchasePrice;
        this.sellPrice = sellPrice;
        this.item_class = item_class;
        this.item_subclass = item_subclass;
    }

    public Item(){
        super();
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

    public int getImage(){
        return image;
    }

    public int getPurchasePrice(){
        return purchasePrice;
    }

    public int getSellPrice(){
        return sellPrice;
    }

    public int getItemClass(){
        return item_class;
    }

    public int getItemSubclass(){
        return item_subclass;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", image=" + image +
                ", purchasePrice=" + purchasePrice +
                ", sellPrice=" + sellPrice +
                ", item_class=" + item_class +
                ", item_subclass=" + item_subclass +
                '}';
    }
}
