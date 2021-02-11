package com.example.mongodb.domain.transaction;

import com.example.mongodb.domain.user.response.UserListResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class MongoTransactionController {

    private final MongoTransactionService mongoTransactionService;

    public MongoTransactionController(MongoTransactionService mongoTransactionService) {
        this.mongoTransactionService = mongoTransactionService;
    }

    @GetMapping("/users")
    public UserListResponse getUsers() {
        return mongoTransactionService.getUsersWithTransaction();
    }
}
