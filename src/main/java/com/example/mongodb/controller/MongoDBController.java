package com.example.mongodb.controller;

import com.example.mongodb.model.User;
import com.example.mongodb.response.ApiCommonResponse;
import com.example.mongodb.response.UserListResponse;
import com.example.mongodb.response.UserResponse;
import com.example.mongodb.service.MongoDBService;
import org.springframework.web.bind.annotation.*;


@RestController
public class MongoDBController {

    MongoDBService mongoDBService;

    public MongoDBController(MongoDBService mongoDBService) {
        this.mongoDBService = mongoDBService;
    }

    @PostMapping(value = "/users")
    public ApiCommonResponse saveUser(@RequestBody User user) {
        return mongoDBService.saveUser(user);
    }

    @GetMapping(value = "/users")
    public UserListResponse getUserList() {
        return mongoDBService.findUserList();
    }

    @GetMapping(value = "/users/{userId}")
    public UserResponse getUser(@PathVariable Long userId) {
        return mongoDBService.getByCollectionKey(userId);
    }

    @PutMapping(value = "/users")
    public UserResponse updateUser(@RequestBody User user) {
        return mongoDBService.updateUserByCollectionKey(user);
    }

    @GetMapping(value = "/rollback/{userId}")
    public ApiCommonResponse rollbackTest(@PathVariable Long userId, boolean isRollback) {
        return mongoDBService.rollbackTest(userId, isRollback);
    }
}
