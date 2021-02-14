package com.example.mongodb.transaction;

import com.example.mongodb.ServiceTest;
import com.example.mongodb.domain.user.UserService;
import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.exception.ValidCustomException;
import com.example.mongodb.utils.TransactionTemplateWrapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoTransactionTest extends ServiceTest {
    @Autowired
    TransactionTemplateWrapper transactionTemplateWrapper;
    @Autowired
    UserService userService;
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void transactionTest() {
        // given
        User user = new User(Long.MAX_VALUE, "test", "000000");

        // when
        try {
            transactionTemplateWrapper.execute(() -> {
                userService.saveUserHandling(user);
                userService.saveUserLogWithTtlIndex(user);

                throw new ValidCustomException(500, "data rollback");
            });
        } catch (Exception ignored) {
        }

        Query query = new Query().addCriteria(Criteria.where(user.getCollectionKey()).is(user.getUserId()));
        User getUser = mongoTemplate.findOne(query, User.class);

        // then
        Assert.assertNull(getUser);
    }
}
