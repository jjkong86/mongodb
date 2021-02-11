package com.example.mongodb.domain.user.repository;

import com.example.mongodb.domain.user.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
    void deleteByUserId(Long useId);
    @NotNull
    List<User> findAll();
}
