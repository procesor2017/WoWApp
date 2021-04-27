package db;
import api.ApiCall;
import model.AuctionItem;
import model.Item;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class SqlDriver {
    private void addNewItemPriceInDatabase(AuctionItem item, Timestamp ts){
        Statement statement = null;
        Connection connection = null;
        Long time = ts.getTime();
        Long auctionUnitPrice = item.getAuctionUnitPrice();
        Long auctionBuyout = item.getAuctionBuyout();
        Integer itemid = item.getId();

        if (auctionBuyout != 0) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/db/ItemDatabase.db");
                connection.setAutoCommit(true);
                statement = connection.createStatement();
                DatabaseMetaData md = connection.getMetaData();
                ResultSet rs = md.getColumns(null, null, "items_price_buyout", time.toString());
                if (!rs.next()) {
                    statement.executeUpdate("ALTER TABLE items_price_buyout ADD COLUMN " + '"' + time.toString() + '"' + " INTEGER");
                }
                statement.executeUpdate("UPDATE items_price_buyout SET " + '"' + time.toString() + '"' + " = " + '"' + auctionBuyout + '"' + " WHERE items_id = " + '"' + itemid + '"');
                //UPDATE items_price SET "1618738474694" = 5 WHERE items_id = 4
                statement.close();
                connection.close();

            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }else if(auctionUnitPrice != 0){
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/db/ItemDatabase.db");
                connection.setAutoCommit(true);
                statement = connection.createStatement();
                DatabaseMetaData md = connection.getMetaData();
                ResultSet rs = md.getColumns(null, null, "items_price_unitPrice", time.toString());
                if (!rs.next()) {
                    statement.executeUpdate("ALTER TABLE items_price_unitPrice ADD COLUMN " + '"' + time.toString() + '"' + " INTEGER");
                }
                statement.executeUpdate("UPDATE items_price_unitPrice SET " + '"' + time.toString() + '"' + " = " + '"' + auctionUnitPrice + '"' + " WHERE items_id = " + '"' + itemid + '"');
                //UPDATE items_price SET "1618738474694" = 5 WHERE items_id = 4
                statement.close();
                connection.close();

            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        System.out.println("Add new price successful");
    }

    public void addItem(Item item){
        Statement statement = null;
        Connection connection = null;
        Integer items_id = item.getId();
        String id = items_id.toString();
        String itemNamebef = item.getName().toString().replace("\"", "");

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/db/ItemDatabase.db");
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO items_price_unitPrice (items_id) VALUES (" + id + ")");
            statement.executeUpdate("INSERT INTO items_price_buyout (items_id) VALUES (" + id + ")");
            statement.executeUpdate("INSERT INTO items (items_id, name, image, buy_price, sell_price, class, subclass) VALUES (" + id + ", " + '"' + itemNamebef + '"' + ", " + item.getImage() + ", " + item.getPurchasePrice() + ", " + item.getSellPrice() + ", " + item.getItemClass() + ", " + item.getItemSubclass() + ")");
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Add items" + itemNamebef + "was successful");
    }

    public void createDBFromItems(ArrayList<Item> auctions_items) {
        System.out.println("INF :: START TRY TO SAVE ITEMS");
        for (int i = 0; i < auctions_items.size(); i++) {
            System.out.println(auctions_items.get(i));
            addItem(auctions_items.get(i));
            System.out.println("Přidán nový item číslo: " + i);
        }
        System.out.println("INF :: DB WAS CREATED");
    }

    public void addAllItemsPriceFromAuctionToDB(ArrayList<AuctionItem> auctionItems,Timestamp ts){
        System.out.println("INF :: START TRY TO ITEMS PRICE SAVE DB");
        for (int i = 0; i < auctionItems.size(); i++) {
            System.out.println(auctionItems.get(i));
            addNewItemPriceInDatabase(auctionItems.get(i), ts);
            System.out.println("Přidána nová cena k itemu ID: " + i);
        }
        System.out.println("INF :: DB WAS CREATED");
    }

    public Item getItemFromDB(Integer id){
        Statement statement = null;
        Connection connection = null;
        String sql = "select * from items left join items_price_buyout on items_price_buyout.items_id = items.items_id join items_price_unitPrice on items_price_unitPrice.items_id = items.items_id where items.items_id=" + id;
        Item item = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/db/ItemDatabase.db");
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            long auctionPrice = 0;

            if (rs.getLong(9)!= 0){
                auctionPrice = rs.getLong(9);
            }else if(rs.getLong(11)!= 0){
                auctionPrice = rs.getLong(11);
            }else{
                auctionPrice = 0;
            }
            item = new Item(rs.getString("name"), rs.getInt("items_id"), rs.getInt("image"), auctionPrice, rs.getInt("buy_price"), rs.getInt("sell_price"), rs.getInt("class"),rs.getInt("subclass"));

            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println(item.getName());
        return item;
    }

    public long getLastItemsPriceFromDB(Integer id){
        Statement statement = null;
        Connection connection = null;
        String sql = "select * from items left join items_price_buyout on items_price_buyout.items_id = items.items_id join items_price_unitPrice on items_price_unitPrice.items_id = items.items_id where items.items_id=" + id;
        Item item = null;
        long auctionPrice = 0;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/db/ItemDatabase.db");
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);


            if (rs.getLong(9)!= 0){
                auctionPrice = rs.getLong(9);
            }else if(rs.getLong(11)!= 0){
                auctionPrice = rs.getLong(11);
            }else{
                auctionPrice = 0;
            }
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println(auctionPrice);
        return auctionPrice;
    }

    public long getLastItemsPriceIn24hFromDB(Integer id){
        Statement statement = null;
        Connection connection = null;
        String sql = "select items_price_buyout.*, items_price_unitPrice.* from items left join items_price_buyout on items_price_buyout.items_id = items.items_id join items_price_unitPrice on items_price_unitPrice.items_id = items.items_id where items.items_id=" + id;
        Item item = null;
        long auctionPrice = 0;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/db/ItemDatabase.db");
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            //Předělat
            if (rs.getLong(9)!= 0){
                auctionPrice = rs.getLong(9);
            }else if(rs.getLong(11)!= 0){
                auctionPrice = rs.getLong(11);
            }else{
                auctionPrice = 0;
            }
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println(auctionPrice);
        return auctionPrice;
    }

}
