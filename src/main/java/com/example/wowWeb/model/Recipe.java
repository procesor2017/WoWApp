package com.example.wowWeb.model;

import java.util.ArrayList;

public class Recipe {

    private Integer id;
    private String name;
    private Integer professionID;
    private Integer subProfessionID;
    private Integer itemId;
    private ArrayList<Integer> reagents;

    public Recipe(Integer id, String name, Integer itemId,Integer professionID, Integer subProfessionID) {
        this.id = id;
        this.name = name;
        this.professionID = professionID;
        this.subProfessionID = subProfessionID;
        this.itemId = itemId;

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getProfessionID() {
        return professionID;
    }

    public Integer getSubProfessionID() {
        return subProfessionID;
    }

    public Integer getItemId() {
        return itemId;
    }

    public ArrayList<Integer> getReagents() {
        return reagents;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfessionID(Integer professionID) {
        this.professionID = professionID;
    }

    public void setSubProfessionID(Integer subProfessionID) {
        this.subProfessionID = subProfessionID;
    }

    public void setReagents(ArrayList<Integer> reagents) {
        this.reagents = reagents;
    }
}
