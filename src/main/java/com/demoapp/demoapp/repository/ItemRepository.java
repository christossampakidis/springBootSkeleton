package com.demoapp.demoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demoapp.demoapp.entity.StripeItem;

@Repository
public interface ItemRepository extends JpaRepository<StripeItem, Long> {
}
