package com.debankar.course_management_system.service;

import com.debankar.course_management_system.entity.User;
import com.debankar.course_management_system.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create a new user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Update an existing user
    public User updateUser(ObjectId id, User userDetails) {
        // You may want to check if the user exists before updating
        if (userRepository.existsById(id)) {
            userDetails.setId(id);
            return userRepository.save(userDetails);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    // Delete user by ID
    public void deleteUser(ObjectId id) {
        userRepository.deleteById(id);
    }

    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findAllByRole(role);
    }
}