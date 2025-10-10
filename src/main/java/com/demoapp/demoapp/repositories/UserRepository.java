package com.demoapp.demoapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demoapp.demoapp.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
