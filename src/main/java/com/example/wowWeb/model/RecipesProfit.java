package com.example.wowWeb.model;

import javax.persistence.*;

@Entity(name = "actual_recipes_profit")
@Table(name = "actual_recipes_profit")
public class RecipesProfit {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "recipe_id")
    private Integer recipe_id;

    @Column(name = "item_id")
    private Integer item_id;

    @Column(name = "name")
    private String name;

    @Column(name = "actual_recipe_price")
    private Integer actual_recipe_price;

    public RecipesProfit(Integer recipe_id, Integer item_id, String name, Integer actual_recipe_price) {
        this.recipe_id = recipe_id;
        this.item_id = item_id;
        this.name = name;
        this.actual_recipe_price = actual_recipe_price;
    }

    public RecipesProfit(){
        super();
    }

    public Integer getRecipe_id() {
        return recipe_id;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public String getName() {
        return name;
    }

    public Integer getActual_recipe_price() {
        return actual_recipe_price;
    }

    @Override
    public String toString() {
        return "RecipesProfit{" +
                "recipe_id=" + recipe_id +
                ", item_id=" + item_id +
                ", name='" + name + '\'' +
                ", actual_recipe_price=" + actual_recipe_price +
                '}';
    }
}
