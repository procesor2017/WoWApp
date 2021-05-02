package com.example.wowWeb.repositories;

import com.example.wowWeb.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    Recipe findById(int id);
    List<Recipe> findAll();

}

