package com.example.mongodb.domain.user;

import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.repository.UserRepository;
import com.example.mongodb.exception.ValidCustomException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserCacheService {

    private final UserRepository userRepository;

    public UserCacheService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(cacheNames="user",key="#userId")
    public <T extends Number> User getUserByUserId(T userId) {
        return this.findUserByUserId(userId);
    }

    public <T extends Number> User findUserByUserId(T userId) {
        return userRepository.findByUserId(userId.longValue())
                .orElseThrow(() -> new ValidCustomException(500, "not found user data by userId."));

    }
}
