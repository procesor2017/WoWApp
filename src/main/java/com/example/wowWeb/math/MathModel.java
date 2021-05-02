package com.example.wowWeb.math;

import com.example.wowWeb.SqlDriver.SqlDriver;
import com.example.wowWeb.model.Item;
import com.example.wowWeb.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;

public class MathModel {
    ArrayList<Integer> tradeSkillItemsList = new ArrayList<>(Arrays.asList(180733, 172056, 172057, 172058, 172059, 178786, 38682, 183952, 20815, 173168, 183954, 183955, 177062));

    public long avgOfArrayList(ArrayList arrayList){
        long sum = 0;
        if(!arrayList.isEmpty()){
            for (Object n : arrayList){
                sum += (long) n;
            }
            return sum / arrayList.size();
        }
        return sum;
    }

    public long advantageRecipeInGoldFromLastPrice(Recipe recipe){
        long sum = 0;
        SqlDriver sqlDriver = new SqlDriver();
        ArrayList<Integer> itemsIdForCrafting = recipe.getReagents();

        for (Integer itemID : itemsIdForCrafting) {
            boolean itemsIsInTrade = false;
            Item item = sqlDriver.getItemFromDB(itemID);
            for(Integer tr_item : tradeSkillItemsList){
                if (tr_item.equals(itemID)){
                    itemsIsInTrade = true;
                }
            }
            if (itemsIsInTrade) {
                sum += item.getPurchasePrice();
            } else {
                long realItemSum = sqlDriver.getLastItemsPriceFromDB(itemID);
                sum += realItemSum;
            }
        }
        long totalRecipePrice = sqlDriver.getLastItemsPriceFromDB(recipe.getItemId()) - sum;
        return totalRecipePrice;
    }

    public long advantageRecipeInGoldFrom24hPrice(Recipe recipe){
        long sum = 0;
        SqlDriver sqlDriver = new SqlDriver();
        ArrayList<Integer> itemsIdForCrafting = recipe.getReagents();

        for (Integer itemID : itemsIdForCrafting){
            sum += sqlDriver.getLastItemsPriceIn24hFromDB(itemID);
        }

        long totalRecipePrice = sqlDriver.getLastItemsPriceIn24hFromDB(recipe.getItemId()) - sum;
        return totalRecipePrice;
    }
}
