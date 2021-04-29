package com.example.wowWeb.controller;

import com.example.wowWeb.SqlDriver.SqlDriver;
import com.example.wowWeb.model.Item;
import com.example.wowWeb.model.ItemPrice;
import com.example.wowWeb.repositories.ItemPriceRepository;
import com.example.wowWeb.repositories.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;


@Controller
public class ItemsController {

    @Autowired
    ItemsRepository itemsRepository;

    @Autowired
    ItemPriceRepository itemPriceRepository;


    @RequestMapping(value="/showItems")
    public String getItems(Model model) {
        model.addAttribute("items", itemsRepository.findAll());
        return "items";
    }

    @RequestMapping(value="/try")
    public String tryIt(Model model) {
        model.addAttribute("itemPriceRepository", itemPriceRepository.findAll());
        return "try";
    }


}
