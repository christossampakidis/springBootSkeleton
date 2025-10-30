package com.demoapp.demoapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demoapp.demoapp.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
