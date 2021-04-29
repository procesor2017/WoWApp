package com.example.wowWeb.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AuctionItem {
    @Id
    @GeneratedValue
    private Integer id;
    private long auctionUnitPrice;
    private long auctionBuyout;

    public AuctionItem(int id, long auctionUnitPrice, long auctionBuyout){
        this.id = id;
        this.auctionUnitPrice = auctionUnitPrice;
        this.auctionBuyout = auctionBuyout;

    }

    public int getId(){
        return id;
    }

    public long getAuctionUnitPrice(){
        return auctionUnitPrice;
    }

    public long getAuctionBuyout(){
        return auctionBuyout;
    }
}
