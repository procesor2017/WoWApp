package com.example.wowWeb.math;

import com.example.wowWeb.SqlDriver.SqlDriver;
import com.example.wowWeb.model.Recipe;

import java.util.ArrayList;

public class MathModel {
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

        for (Integer itemID : itemsIdForCrafting){
            sum += sqlDriver.getLastItemsPriceFromDB(itemID);
        }
        System.out.println("sum: " + sum);
        long totalRecipePrice = sqlDriver.getLastItemsPriceFromDB(recipe.getItemId()) - sum;
        System.out.println(totalRecipePrice);
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
