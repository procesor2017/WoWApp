package com.example.wowWeb.controller;

import com.example.wowWeb.model.Item;
import com.example.wowWeb.repositories.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Set;

@Controller
public class ItemsController {

    @Autowired
    ItemsRepository itemsRepository;

    @RequestMapping(value="/showItems")
    public String getItems(Model model) {
        model.addAttribute("items", itemsRepository.findAll());
        return "items";
    }


}
