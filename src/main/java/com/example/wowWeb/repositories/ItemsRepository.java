package com.example.wowWeb.repositories;

import com.example.wowWeb.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ItemsRepository extends JpaRepository<Item, Integer> {
    Item findById(int id);
    List<Item> findAll();

}

