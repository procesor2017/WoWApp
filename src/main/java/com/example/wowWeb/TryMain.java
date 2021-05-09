package com.example.wowWeb;


import com.example.wowWeb.SqlDriver.SqlDriver;
import com.example.wowWeb.apiCall.ApiCall;
import com.example.wowWeb.math.MathModel;

import java.sql.Timestamp;

public class TryMain {
    public static void main(String[] args){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        SqlDriver sqlDriver = new SqlDriver();
        ApiCall apiCall = new ApiCall();
        MathModel mathModel = new MathModel();
        /*
        ===Full script for creating new DB===

        ApiCall apiCall = new ApiCall();
        SqlDriver sqlDriver = new SqlDriver();

        apiCall.getToken();
        apiCall.createFullAcutionList();
        sqlDriver.createDBFromItems(apiCall.auctions_items);

        apiCall.createNewPriceFromAuction();
        sqlDriver.addAllItemsPriceFromAuctionToDB(apiCall.auctions, ts);
        */
        /*
        === Check DB ===
        SqlDriver sqlDriver = new SqlDriver();
        sqlDriver.getItemFromDB(4358);
        sqlDriver.getLastItemsPriceFromDB(4358);

        === New recipe Db ===
        ApiCall apiCall = new ApiCall();
        apiCall.getToken();

        int i = apiCall.getSubProfession(164);
        System.out.println(i);

        apiCall.getRecipeID(164, 2751);
        sqlDriver.addRecipeToDb(apiCall.getRecipe(164, 2751,42363));

        === Create recipes db ===
        SqlDriver sqlDriver = new SqlDriver();
        sqlDriver.createRecipeDb();         // Vytvořeníá databáze
        sqlDriver.setRecipesAllPRiceInDB(); // Update cen

        SqlDriver sqlDriver = new SqlDriver();
        sqlDriver.getAllItemsFromDbAccordingToProfession(164);

        ==== Aktualizace ITemu na základě receptu ====
        SqlDriver sqlDriver = new SqlDriver();
        sqlDriver.addsItemsFromRecipes();


        === Dostání ceny z mathmodelu ===
        mathModel.advantageRecipeInGoldFromLastPrice(sqlDriver.getRecipeFromDB(42406));
           */
        // === Vytvoření ceny z aukce do db===
        //vždy třeba pro aktualizaci cen
        apiCall.getToken();
        apiCall.createNewPriceFromAuction();
        sqlDriver.addAllItemsPriceFromAuctionToDB(apiCall.auctions, ts);

        //sqlDriver.setRecipesAllPRiceInDB();


    }
}
