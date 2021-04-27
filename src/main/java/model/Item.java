package model;

public class Item {
    private String name;
    private Integer id;
    private long auctionPrice = 0;
    private Integer image;
    private Integer purchasePrice = 0;
    private Integer sellPrice = 0;
    private Integer item_class = 0;
    private Integer item_subclass = 0;

    public Item(String name, int id, int image, long auctionPrice, int purchasePrice, int sellPrice, int item_class, int item_subclass){
        this.name = name;
        this.id = id;
        this.auctionPrice = auctionPrice;
        this.image = image;
        this.purchasePrice = purchasePrice;
        this.sellPrice = sellPrice;
        this.item_class = item_class;
        this.item_subclass = item_subclass;
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

    public long getAuctionPrice(){
        return auctionPrice;
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

}
