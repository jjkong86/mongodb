package com.example.mongodb.domain.cache;

import com.example.mongodb.domain.user.UserService;
import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@Slf4j
public class CacheTestService {

    private final UserService userService;

    public CacheTestService(UserService userService) {
        this.userService = userService;
    }

    @Cacheable(cacheNames="user",key="#userId")
    public <T extends Number> UserResponse getUserByUserId(T userId) {
        return userService.getByCollectionKey(User.builder().userId(userId.longValue()).build());
    }
}
