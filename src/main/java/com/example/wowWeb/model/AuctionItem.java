package com.example.wowWeb.model;

public class AuctionItem {
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
