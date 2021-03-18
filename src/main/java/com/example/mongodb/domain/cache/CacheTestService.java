package com.example.mongodb.domain.cache;

import com.example.mongodb.constant.Constant;
import com.example.mongodb.domain.user.UserService;
import com.example.mongodb.domain.user.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheTestService {

    private final UserService userService;

    public CacheTestService(UserService userService) {
        this.userService = userService;
    }

    public <T extends Number> UserResponse getUserByUserId(T userId) {
        return UserResponse.builder()
                .user(userService.getUserByUserId(userId))
                .code(Constant.CODE_SUCCESS.getCode())
                .build();
    }
}
