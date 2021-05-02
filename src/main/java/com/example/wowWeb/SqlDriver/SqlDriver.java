package com.example.wowWeb.SqlDriver;

import com.example.wowWeb.apiCall.ApiCall;
import com.example.wowWeb.math.MathModel;
import com.example.wowWeb.model.AuctionItem;
import com.example.wowWeb.model.Item;
import com.example.wowWeb.model.ItemPrice;
import com.example.wowWeb.model.Recipe;

import java.sql.*;
import java.util.*;

public class SqlDriver {
    String sqlDbUrl = "jdbc:sqlite:ItemDatabase.db";
    private void addNewItemPriceInDatabase(AuctionItem item, Timestamp ts){
        Statement statement = null;
        Connection connection = null;
        Long time = ts.getTime();
        Long auctionUnitPrice = item.getAuctionUnitPrice();
        Long auctionBuyout = item.getAuctionBuyout();
        Integer itemid = item.getId();

        if (auctionBuyout != 0) {
            try {
                connection = DriverManager.getConnection(sqlDbUrl);
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
                connection = DriverManager.getConnection(sqlDbUrl);
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
            connection = DriverManager.getConnection(sqlDbUrl);
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
            connection = DriverManager.getConnection(sqlDbUrl);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()){
                item = new Item(rs.getString("name"), rs.getInt("items_id"), rs.getInt("image"), rs.getInt("buy_price"), rs.getInt("sell_price"), rs.getInt("class"),rs.getInt("subclass"));
            }else{
                System.out.println("ITEM NOT FOUND");
            }
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return item;
    }

    public long getLastItemsPriceFromDB(Integer id){
        Statement statement = null;
        Connection connection = null;
        String sql = "select * from items left join items_price_buyout on items_price_buyout.items_id = items.items_id join items_price_unitPrice on items_price_unitPrice.items_id = items.items_id where items.items_id=" + id;
        Item item = null;
        long auctionPrice = 0;

        try {
            connection = DriverManager.getConnection(sqlDbUrl);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            if(rs.next()) {
                if (rs.getLong(columnsNumber - 2) != 0) {
                    auctionPrice = rs.getLong(columnsNumber - 2);
                } else if (rs.getLong(columnsNumber) != 0) {
                    auctionPrice = rs.getLong(columnsNumber);
                }
            }else{
                auctionPrice = 0;
                System.out.println("Item with: "+ id +" doesnt exist on auction");
            }
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Celková cena za item na aukci je: " + auctionPrice);
        return auctionPrice;
    }

    public long getLastItemsPriceIn24hFromDB(Integer id){
        Statement statement = null;
        Connection connection = null;
        String sql = "select items_price_buyout.*, items_price_unitPrice.* from items left join items_price_buyout on items_price_buyout.items_id = items.items_id join items_price_unitPrice on items_price_unitPrice.items_id = items.items_id where items.items_id=" + id;
        long auctionPrice = 0;
        ArrayList priceList = new ArrayList();

        try {
            connection = DriverManager.getConnection(sqlDbUrl);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            for (int i = columnsNumber; i > columnsNumber-48; i = i - 4){
                System.out.println("špatně");
                if (i <= 4){
                    System.out.println("NEdostatečný počet sloupcu");
                    MathModel mathModel = new MathModel();
                    long avg = mathModel.avgOfArrayList(priceList);
                    return avg;
                }
                if (rs.getLong(i-2)!= 0){
                    auctionPrice = rs.getLong(i-2);
                }else if(rs.getLong(i)!= 0){
                    auctionPrice = rs.getLong(i);
                }
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        MathModel mathModel = new MathModel();
        long avg = mathModel.avgOfArrayList(priceList);
        return avg;
    }

    public void addRecipeToDb(Recipe recipe){
        Statement statement = null;
        Connection connection = null;
        Integer recipe_id = recipe.getId();
        String id = recipe_id.toString();
        String recipeNamebef = recipe.getName().toString().replace("\"", "");

        try {
            connection = DriverManager.getConnection(sqlDbUrl);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO recipes_price (recipe_id) VALUES (" + id + ")");
            statement.executeUpdate("INSERT INTO recipes (recipe_id, name, professionID, subProfessionID, item_id) VALUES (" + id + ", " + '"' + recipeNamebef + '"' + ", " + recipe.getProfessionID() + ", " + recipe.getSubProfessionID() + ", " + recipe.getItemId() +")");
            for (int i = 0; i < recipe.getReagents().size(); i++){
                String columnName = "reagent" + i;
                statement.executeUpdate("UPDATE recipes SET " + '"' + columnName + '"' + " = " + '"' + recipe.getReagents().get(i) + '"' + " WHERE recipe_id = " + '"' + id + '"');
            }
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Add recipes: " + recipeNamebef + " was successful");
    }

    public void createRecipeDb(){
        ApiCall apiCall = new ApiCall();
        apiCall.getToken();
        int receptNumber = 0;
        List<Integer> prof_list = Arrays.asList(164, 165, 171, 185, 197, 202, 333, 755, 773);

        for (int i = 0; i < prof_list.size(); i++){
            int subProfId = apiCall.getSubProfession(prof_list.get(i));
            ArrayList<Integer> recipeList = apiCall.getRecipeID(prof_list.get(i), subProfId);

            for (int j = 0; j<recipeList.size(); j++){
                addRecipeToDb(apiCall.getRecipe(prof_list.get(i), subProfId, recipeList.get(j)));
                receptNumber++;
            }
        }
        System.out.println("Přídáno: " + receptNumber + " receptů");
    }

    public Recipe getRecipeFromDB(Integer recipeID){
        Statement statement = null;
        Connection connection = null;
        Recipe recipe = null;
        ArrayList<Integer> reagentList= new ArrayList<>();

        try {
            connection = DriverManager.getConnection(sqlDbUrl);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM recipes where recipe_id = " + recipeID );
            recipe = new Recipe(rs.getInt(1), rs.getString(2), rs.getInt(5), rs.getInt(3), rs.getInt(4));

            //Protože sloupcu pro reagenta v tabulce je 10
            for (int i = 6; i < 15; i++){
                if (rs.getInt(i) != 0){
                    reagentList.add(rs.getInt(i));
                }
            }
            recipe.setReagents(reagentList);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return recipe;
    }

    public ArrayList<Integer> getAllRecipeFromDB(){
        Statement statement = null;
        Connection connection = null;
        ArrayList<Integer> recipeList = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(sqlDbUrl);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT recipe_id FROM recipes");

            while(rs.next()){
                int recipe_id = rs.getInt("recipe_id");
                recipeList.add(recipe_id);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println(recipeList);
        return recipeList;
    }

    public void setRecipePriceInDB(Recipe recipe, Timestamp ts, long itemPrice){
        Statement statement = null;
        Connection connection = null;
        Long time = ts.getTime();
        long price = itemPrice;
        Integer recipeId = recipe.getId();

        try {
            connection = DriverManager.getConnection(sqlDbUrl);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getColumns(null, null, "recipes_price", time.toString());
            if (!rs.next()) {
                statement.executeUpdate("ALTER TABLE recipes_price ADD COLUMN " + '"' + time.toString() + '"' + " INTEGER");
            }
            statement.executeUpdate("UPDATE recipes_price SET " + '"' + time.toString() + '"' + " = " + '"' + price + '"' + " WHERE recipe_id = " + '"' + recipeId + '"');
            statement.executeUpdate("UPDATE actual_recipes_profit SET actual_recipe_price =" + price + " WHERE recipe_id = " + '"' + recipeId + '"');
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Add new price successful");
    }

    public void setRecipesAllPRiceInDB(){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        MathModel mathModel = new MathModel();
        ArrayList<Integer> recipeList = getAllRecipeFromDB();
        for (Integer recipe_id : recipeList){
            Recipe recipe = getRecipeFromDB(recipe_id);
            long recipe_price = mathModel.advantageRecipeInGoldFromLastPrice(getRecipeFromDB(recipe.getId()));
            setRecipePriceInDB(recipe, ts, recipe_price);
        }
    }

    public ArrayList<Item> getAllItemsFromDbAccordingToProfession(Integer professionID){
        Statement statement = null;
        Connection connection = null;
        String sql = "select * from recipes join items on items.items_id = recipes.item_id where professionID =" + professionID;
        ArrayList<Item> itemList = new ArrayList<>();
        Item item;

        try {
            connection = DriverManager.getConnection(sqlDbUrl);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                item = new Item(rs.getString(17), rs.getInt(16), rs.getInt("image"), rs.getInt("buy_price"), rs.getInt("sell_price"), rs.getInt("class"),rs.getInt("subclass"));
                itemList.add(item);
                System.out.println("INF :: Item přidán");
            }

            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return itemList;
    }

    public ArrayList<ItemPrice> getAllItemsPriceFromDb(Integer professionID){
        Statement statement = null;
        Connection connection = null;
        String sql = "select * from recipes join items on items.items_id = recipes.item_id " +
                "join items_price_buyout on items_price_buyout.items_id = items.items_id " +
                "join items_price_unitPrice on items_price_unitPrice.items_id=items.items_id " +
                "join recipes_price on recipes.recipe_id = recipes_price.recipe_id where professionID =" + professionID;
        ArrayList<ItemPrice> itemPricesList = new ArrayList<>();


        try {
            connection = DriverManager.getConnection(sqlDbUrl);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()){
                ItemPrice itemPrice = new ItemPrice(rs.getInt(16), rs.getInt(1), rs.getString(17), rs.getLong(columnsNumber));
                itemPricesList.add(itemPrice);
            }

            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return itemPricesList;
    }

    public void addsItemsFromRecipes(){
        ArrayList<Integer> recipeList = new ArrayList<>();
        recipeList= getAllRecipeFromDB();

        for(int i = 0; i < recipeList.size(); i++){
            ApiCall apiCall = new ApiCall();
            apiCall.getToken();
            Recipe recipe = getRecipeFromDB(recipeList.get(i));
            System.out.println(recipe.getItemId());
            Item item = apiCall.getItem(recipe.getItemId());
            System.out.println(item.toString());
            if (getItemFromDB(item.getId()) == null){
                addItem(item);
            }else{
                System.out.println("INF :: ITEM EXISTS");
            }
        }
    }

}
