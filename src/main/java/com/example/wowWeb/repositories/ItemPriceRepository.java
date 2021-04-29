package com.example.wowWeb.repositories;

import com.example.wowWeb.model.ItemPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPriceRepository extends JpaRepository<ItemPrice, Integer> {
   List<ItemPrice> findAll();
}
