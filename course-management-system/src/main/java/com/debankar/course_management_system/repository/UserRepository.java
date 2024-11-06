package com.debankar.course_management_system.repository;

import com.debankar.course_management_system.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByEmail(String email);
    List<User> findAllByRole(User.Role role);
}
