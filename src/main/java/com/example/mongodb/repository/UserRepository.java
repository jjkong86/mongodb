package com.example.mongodb.repository;

import com.example.mongodb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, Long> {
    User findByUserId(Long userId);
    void deleteByUserId(Long useId);
    List<User> findAll();
}
