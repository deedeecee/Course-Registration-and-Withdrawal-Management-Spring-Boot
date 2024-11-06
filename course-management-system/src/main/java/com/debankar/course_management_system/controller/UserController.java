package com.debankar.course_management_system.controller;

import com.debankar.course_management_system.dto.Mapper;
import com.debankar.course_management_system.dto.UserCreationDTO;
import com.debankar.course_management_system.dto.UserDTO;
import com.debankar.course_management_system.entity.User;
import com.debankar.course_management_system.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final Mapper mapper;

    @Autowired
    public UserController(UserService userService, Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    // create a new user
    @PostMapping
    public ResponseEntity<UserCreationDTO> createUser(@RequestBody UserCreationDTO userDTO) {
//        User createdUser = userService.createUser(user);
//        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

        User user = mapper.toUser(userDTO);

        userService.createUser(user);

        System.out.println("Created User with User ID: " + user.getId());

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    // get all users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream()
                .map(mapper::toUserDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable ObjectId id) {
        Optional<User> user = userService.getUserById(id);

        return user.map(value -> ResponseEntity.ok(mapper.toUserDTO(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable ObjectId id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable ObjectId id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
