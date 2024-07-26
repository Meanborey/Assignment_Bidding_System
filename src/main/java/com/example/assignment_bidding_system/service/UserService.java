package com.example.assignment_bidding_system.service;

import com.example.assignment_bidding_system.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String name);


    User createUser(String username, String password);
}
