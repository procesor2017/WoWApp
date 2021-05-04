package com.example.wowWeb.repositories;

import com.example.wowWeb.model.Item;
import com.example.wowWeb.model.Recipe;
import com.example.wowWeb.model.RecipesProfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface RecipesProfitRepository extends JpaRepository<RecipesProfit, Integer> {
    RecipesProfit findById(int id);
    List<RecipesProfit> findAll();


    @Query(value = "select * from actual_recipes_profit " +
            "left join recipes on recipes.recipe_id = actual_recipes_profit.recipe_id where professionID = ?1 order by actual_recipe_price DESC", nativeQuery = true)
    List<RecipesProfit> findRecipeAccordToProfession(@PathVariable Integer professionID);

    @Query(value = "select * from actual_recipes_profit " +
            "left join recipes on recipes.recipe_id = actual_recipes_profit.recipe_id order by actual_recipe_price DESC", nativeQuery = true)
    List<RecipesProfit> findAllRecipeWithProfit();

}

