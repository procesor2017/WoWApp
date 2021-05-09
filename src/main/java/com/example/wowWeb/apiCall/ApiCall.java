package com.example.wowWeb.apiCall;

import com.example.wowWeb.model.AuctionItem;
import com.example.wowWeb.model.Item;
import com.example.wowWeb.model.Recipe;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;


public class ApiCall {
    public ArrayList<AuctionItem> auctions = new ArrayList<>();  // List s daty z aukce v podobě json
    public ArrayList<Item> auctions_items = new ArrayList<>(); // List jen s daty Item: cena, sllouží k aktualizaci dat v db
    String token;

    public void getToken(){
        String tokenUrl = "https://us.battle.net/oauth/token";
        // === NAstavení proměných pro github actions ====
        String clientId = System.getenv("clientID");
        String clientSecret = System.getenv("clientSecret");
        String auth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        String requestBody = "grant_type=client_credentials";

        JSONParser parser = new JSONParser();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://us.battle.net/oauth/token"))
                .setHeader("Authorization", "Basic " + auth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(response.body());

        String tokenString = response.body();
        // Parse response
        try {
            JSONObject json = (JSONObject) parser.parse(tokenString);
            tokenString = (String) json.get("access_token");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(tokenString);
        token = tokenString;
    }

    private void getItemPriceFromAuction(String accesToken){
        String url = "https://eu.api.blizzard.com/data/wow/connected-realm/1329/auctions?namespace=dynamic-eu&locale=en_GB&access_token=" + accesToken;
        JSONParser parser = new JSONParser();
        ArrayList data_list = new ArrayList();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Parse response
        try {
            JSONObject json = (JSONObject) parser.parse(response.body());
            data_list = (ArrayList) json.get("auctions");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < data_list.size(); i++){
            JSONObject data_0 = (JSONObject) data_list.get(i);
            if (data_0.get("unit_price") != null ) {
                long itemPrice = Long.parseLong(data_0.get("unit_price").toString().replace("\"", ""));
                JSONObject iId = (JSONObject) data_0.get("item");
                Integer itemID = Integer.parseInt(iId.get("id").toString());
                AuctionItem auctionItem = new AuctionItem(itemID, itemPrice, 0);
                auctions.add(auctionItem);
            }else if(data_0.get("buyout") != null){
                long itemPrice = Long.parseLong(data_0.get("buyout").toString().replace("\"", ""));
                JSONObject iId = (JSONObject) data_0.get("item");
                Integer itemID = Integer.parseInt(iId.get("id").toString());
                AuctionItem auctionItem = new AuctionItem(itemID, 0, itemPrice);
                auctions.add(auctionItem);
            }else{
                JSONObject iId = (JSONObject) data_0.get("item");
                Integer itemID = Integer.parseInt(iId.get("id").toString());
                System.out.println("Item:" + itemID + " is null cant be added");
            }
        }
    }

    private void findAndSaveItemWithClassSubclass(Integer itemClass, Integer itemSubclass,String accesToken){
        //Add items to database items
        int page = 1;
        int pageCount = 1;
        String url = "https://eu.api.blizzard.com/data/wow/search/item?namespace=static-eu&locale=en_GB&orderby=id&_page=" + page +"&item_class.id=" + itemClass + "&item_subclass.id=" + itemSubclass + "&access_token=" + accesToken;
        JSONParser parser = new JSONParser();
        ArrayList resultsList = new ArrayList();

        HttpRequest request_1 = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response_1 = null;
        try {
            response_1 = client.send(request_1, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject json = (JSONObject) parser.parse(response_1.body());
            String pageString = json.get("page").toString();
            page = Integer.parseInt(pageString);
            pageCount = Integer.parseInt(json.get("pageCount").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        while (page <= pageCount){
            String new_url = "https://eu.api.blizzard.com/data/wow/search/item?namespace=static-eu&locale=en_GB&orderby=id&_page=" + page +"&item_class.id=" + itemClass + "&item_subclass.id=" + itemSubclass + "&access_token=" + accesToken;
            HttpRequest new_request_2 = HttpRequest.newBuilder()
                    .uri(URI.create(new_url))
                    .build();

            HttpClient new_client_2 = HttpClient.newBuilder().build();
            HttpResponse<String> new_response_2 = null;
            try {
                new_response_2 = new_client_2.send(new_request_2, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                JSONObject json = (JSONObject) parser.parse(new_response_2.body());
                page = Integer.parseInt(json.get("page").toString());
                resultsList = (ArrayList) json.get("results");
                System.out.println("Page count is: " + pageCount + " and page is: " + page);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < resultsList.size(); i++){
                JSONObject data_0 = (JSONObject) resultsList.get(i);
                JSONObject data = (JSONObject) data_0.get("data");
                JSONObject iName = (JSONObject) data.get("name");
                String item_name = iName.get("en_GB").toString();
                Integer item_id = Integer.parseInt(data.get("id").toString());
                JSONObject iImage = (JSONObject) data.get("media");
                Integer item_image  = Integer.parseInt(iImage.get("id").toString());
                Integer purchase_quantity = Integer.parseInt(data.get("purchase_quantity").toString());
                Integer item_purchasePrice = Integer.parseInt(data.get("purchase_price").toString()) / purchase_quantity;
                Integer item_sellPrice = Integer.parseInt(data.get("sell_price").toString());
                JSONObject iClass = (JSONObject) data.get("item_class");
                Integer item_class = Integer.parseInt(iClass.get("id").toString());
                JSONObject iSubclass = (JSONObject) data.get("item_subclass");
                Integer item_subclass = Integer.parseInt(iSubclass.get("id").toString());

                Item item = new Item(item_name, item_id, item_image, item_purchasePrice, item_sellPrice, item_class, item_subclass);
                auctions_items.add(item);
            }
            page += 1;
        }
    }

    private Integer getSubclass(Integer classNumber, String accesToken){
        String url = "https://us.api.blizzard.com/data/wow/item-class/" + classNumber +"?namespace=static-us&locale=en_US&access_token=" + accesToken;
        JSONParser parser = new JSONParser();
        Integer numberSubClass = 0;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONObject json = (JSONObject) parser.parse(response.body());
            JSONArray sublcass = (JSONArray) json.get("item_subclasses");
            numberSubClass = sublcass.size();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberSubClass;
    }

    public void createFullAcutionList(){
        //create complet db; add every calss and sublcass to db
        Integer numberClass = 17; //viz documentation/classAndSubclass.md 17
        for (int i = 0; i < numberClass; i++){
            System.out.println("Number class is: " + i);
            if(!(i == 10 || i == 14)){
                Integer nSbuClass = getSubclass(i, token);
                for (int j = 0; j < nSbuClass; j++){
                    System.out.println("Number subclass is: " + j);
                    findAndSaveItemWithClassSubclass(i,j, token);
                }
            }else{
                System.out.println("INF :: Subclass doenst know 10 or 14 numbers.");
            }
        }
    }

    public void createNewPriceFromAuction(){
        getItemPriceFromAuction(token);
    }

    public int getSubProfession(Integer profesionID){
        String url = "https://us.api.blizzard.com/data/wow/profession/"+ profesionID +"?namespace=static-us&locale=en_US&access_token=" + token;
        JSONParser parser = new JSONParser();
        ArrayList data_list = new ArrayList();
        int subProfession = 0;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Parse response
        try {
            System.out.println(response);
            JSONObject json = (JSONObject) parser.parse(response.body());
            data_list = (ArrayList) json.get("skill_tiers");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < data_list.size(); i++){
            JSONObject data_0 = (JSONObject) data_list.get(i);
            if (data_0.get("name").toString().contains("Shadowlands")){
                subProfession = Integer.parseInt(data_0.get("id").toString());
                System.out.println("INF :: subprofession find: " + subProfession);
            }
        }
        return subProfession;
    }

    public ArrayList<Integer> getRecipeID(Integer professionID, Integer subProfessionID){
        String url = "https://us.api.blizzard.com/data/wow/profession/"+ professionID +"/skill-tier/"+ subProfessionID +"?namespace=static-us&locale=en_US&access_token=" + token;
        JSONParser parser = new JSONParser();
        ArrayList data_list_0 = new ArrayList();
        ArrayList data_list = new ArrayList();
        JSONObject data_list_1;
        ArrayList<Integer> listOfRecipe = new ArrayList<>();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Parse response
        try {
            JSONObject json = (JSONObject) parser.parse(response.body());
            data_list_0 = (ArrayList) json.get("categories");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < data_list_0.size(); i++){
            data_list_1 = (JSONObject) data_list_0.get(i);
            data_list = (ArrayList) data_list_1.get("recipes");
            for (int j = 0; j < data_list.size(); j++){
                JSONObject data_0 = (JSONObject) data_list.get(j);
                listOfRecipe.add(Integer.parseInt(data_0.get("id").toString()));
            }
        }
        return listOfRecipe;
    }

    public Recipe getRecipe(Integer professionID, Integer subProfessionID, Integer recipeID){
        String url = "https://us.api.blizzard.com/data/wow/recipe/"+ recipeID +"?namespace=static-us&locale=en_US&access_token=" + token;
        JSONParser parser = new JSONParser();
        ArrayList data_list_reagents = new ArrayList();
        ArrayList data_list_modified_slots = new ArrayList();
        ArrayList<Recipe> recipeList = new ArrayList<>();
        JSONObject data_list_0;
        JSONObject data_list_1;
        ArrayList<Integer> reagentsId = new ArrayList<>();
        String name = "";
        Integer itemID = 0;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Parse response
        try {
            JSONObject json = (JSONObject) parser.parse(response.body());
            name = json.get("name").toString();
            data_list_reagents = (ArrayList) json.get("reagents");
            data_list_1 = (JSONObject) json.get("crafted_item");
            if (data_list_1 == null){
                itemID = Integer.parseInt(json.get("id").toString());
            }else{
                itemID = Integer.parseInt(data_list_1.get("id").toString());
            }
            data_list_modified_slots = (ArrayList) json.get("modified_crafting_slots");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < data_list_reagents.size(); i++){
            data_list_0 = (JSONObject) data_list_reagents.get(i);
            JSONObject reagent = (JSONObject) data_list_0.get("reagent");
            reagentsId.add(Integer.parseInt(reagent.get("id").toString()));
        }
        if (data_list_modified_slots != null) {
            for (int i = 0; i < data_list_modified_slots.size(); i++) {
                data_list_0 = (JSONObject) data_list_modified_slots.get(i);
                JSONObject reagent = (JSONObject) data_list_0.get("slot_type");
                reagentsId.add(Integer.parseInt(reagent.get("id").toString()));
            }
        }

        Recipe recipe = new Recipe(recipeID, name, itemID, professionID, subProfessionID);
        recipe.setReagents(reagentsId);
        return recipe;
    }

    public Item getItem(Integer itemID){
        String url = "https://us.api.blizzard.com/data/wow/item/" + itemID + "?namespace=static-us&locale=en_US&access_token=" + token;
        JSONParser parser = new JSONParser();
        Item item = null;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Parse response
        try {
            JSONObject json = (JSONObject) parser.parse(response.body());
            String name =  json.get("name").toString();
            int itemId = Integer.parseInt(json.get("id").toString());

            JSONObject item_class = (JSONObject) json.get("item_class");
            JSONObject item_subclass = (JSONObject) json.get("item_subclass");
            int itemClass = Integer.parseInt(item_class.get("id").toString());
            int itemSublclass = Integer.parseInt(item_subclass.get("id").toString());

            int purchase_price = Integer.parseInt(json.get("purchase_price").toString());
            int sell_price = Integer.parseInt(json.get("sell_price").toString());

            JSONObject item_image = (JSONObject) json.get("media");
            int itemImage = Integer.parseInt(item_image.get("id").toString());

            item = new Item(name, itemId, itemImage, purchase_price, sell_price, itemClass, itemSublclass);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return item;
    }
}

