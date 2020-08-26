package com.example.mongodb.repository;

import com.example.mongodb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {
    public User findByUserId(Long userId);
    public User findByName(String name);
    public void deleteByUserId(Long useId);
}
