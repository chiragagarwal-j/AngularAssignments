package com.Rest.forum.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rest.forum.entity.User;
import com.Rest.forum.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // Inject the UserRepository

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Find a user by name
    public Optional<User> findUserByName(String username) {
        return userRepository.findByName(username);
    }

    // Create a new user
    public User createUser(User user) {
        // You can add validation logic here if needed
        return userRepository.save(user);
    }

    // List all users
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
}
