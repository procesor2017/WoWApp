package com.example.wowWeb.model;

import javax.persistence.*;
import java.util.ArrayList;

@Entity(name = "recipes")
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "recipe_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "professionID")
    private Integer professionID;

    @Column(name = "subProfessionID")
    private Integer subProfessionID;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "reagent0")
    private Integer reagent0;

    @Column(name = "reagent1")
    private Integer reagent1;

    @Column(name = "reagent2")
    private Integer reagent2;

    @Column(name = "reagent3")
    private Integer reagent3;

    @Column(name = "reagent4")
    private Integer reagent4;

    @Column(name = "reagent5")
    private Integer reagent5;

    @Column(name = "reagent6")
    private Integer reagent6;

    @Column(name = "reagent7")
    private Integer reagent7;

    @Column(name = "reagent8")
    private Integer reagent8;

    @Column(name = "reagent9")
    private Integer reagent9;

    private ArrayList<Integer> reagents;

    public Recipe(Integer id, String name, Integer itemId, Integer professionID, Integer subProfessionID) {
        this.id = id;
        this.name = name;
        this.professionID = professionID;
        this.subProfessionID = subProfessionID;
        this.itemId = itemId;
    }

    public Recipe(){
        super();
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
