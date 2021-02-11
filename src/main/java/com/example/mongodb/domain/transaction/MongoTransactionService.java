package com.example.mongodb.domain.transaction;

import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.response.UserListResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
public class MongoTransactionService {
    private final MongoTransactionManager mongoTransactionManager;
    private final MongoTemplate mongoTemplate;

    public MongoTransactionService(MongoTransactionManager mongoTransactionManager, MongoTemplate mongoTemplate) {
        this.mongoTransactionManager = mongoTransactionManager;
        this.mongoTemplate = mongoTemplate;
    }

    public UserListResponse getUsersWithTransaction() {
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);

        TransactionTemplate transactionTemplate = new TransactionTemplate(mongoTransactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NotNull TransactionStatus status) {
                mongoTemplate.insert(new User(123L, "Kim", "20"));
                mongoTemplate.insert(new User(1234L, "Jack", "45"));
            }
        });

        Query query = new Query().addCriteria(Criteria.where("name").is("Jack"));
        List<User> users = mongoTemplate.find(new Query(), User.class);
        System.out.println(users.size());

        return UserListResponse.builder().users(users).build();
    }

}
