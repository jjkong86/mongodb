package com.example.mongodb.domain.transaction;

import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.response.UserResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class MongoTransactionController {

    private final MongoTransactionService mongoTransactionService;

    public MongoTransactionController(MongoTransactionService mongoTransactionService) {
        this.mongoTransactionService = mongoTransactionService;
    }

    @PostMapping("/users")
    public UserResponse getUsers(@RequestBody User user) {
        return mongoTransactionService.saveUserWithTransaction(user);
    }

    @PostMapping("/rollback")
    public UserResponse rollbackTest(@RequestBody User user, boolean isRollBack) {
        return mongoTransactionService.rollBackTest(user, isRollBack);
    }

    @PostMapping("/rollback/annotation")
    public UserResponse annotationRollBackTest(@RequestBody User user, boolean isRollBack) {
        return mongoTransactionService.annotationRollBackTest(user, isRollBack);
    }
    @PostMapping("/rollback/annotation/requires/new")
    public UserResponse annotationRollBackWithRequiresNew(@RequestBody User user, boolean isRollBack) {
        return mongoTransactionService.rollBackWithRequiresNew(user, isRollBack);
    }

}
