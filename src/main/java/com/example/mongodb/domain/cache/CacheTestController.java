package com.example.mongodb.domain.cache;

import com.example.mongodb.domain.user.response.UserResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
public class CacheTestController {

    private final CacheTestService cacheTestService;

    public CacheTestController(CacheTestService cacheTestService) {
        this.cacheTestService = cacheTestService;
    }

    @GetMapping(value = "/users/{userId}")
    public UserResponse getUser(@PathVariable Long userId) {
        return cacheTestService.getUserByUserId(userId);
    }
}
