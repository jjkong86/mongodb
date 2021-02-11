package com.example.mongodb;

import com.example.mongodb.domain.user.model.User;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTransactionTest {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoTransactionManager mongoTransactionManager;


    @Test
    public void givenTransactionTemplate_whenPerformTransaction_thenSuccess() {
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);

        TransactionTemplate transactionTemplate = new TransactionTemplate(mongoTransactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NotNull TransactionStatus status) {
                mongoTemplate.insert(new User(123L, "Kim", "20"));
                mongoTemplate.insert(new User(1234L, "Jack", "45"));
            }

            ;
        });

        Query query = new Query().addCriteria(Criteria.where("userId").is(1));
        List<User> users = mongoTemplate.find(query, User.class);

        System.out.println(users.size());
    }
}
