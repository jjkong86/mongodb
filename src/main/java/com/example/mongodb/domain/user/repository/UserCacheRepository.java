package com.example.mongodb.domain.user.repository;


import com.example.mongodb.domain.user.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserCacheRepository extends MongoRepository<User, Long> {

    @Cacheable(value = "users", unless = "#a0=='Foundation'")
    Optional<User> findByUserId(Long userId);
}
