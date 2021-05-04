package com.example.wowWeb.controller;

import com.example.wowWeb.repositories.ItemsRepository;
import com.example.wowWeb.repositories.RecipesProfitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    RecipesProfitRepository recipesProfitRepository;

    @GetMapping("/")
    String index(Model model) {
        model.addAttribute("recipesProfit", recipesProfitRepository.findAllRecipeWithProfit());
        return "index";
    }


}
