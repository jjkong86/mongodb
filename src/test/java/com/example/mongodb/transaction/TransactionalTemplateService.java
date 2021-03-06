package com.example.mongodb.transaction;

import com.example.mongodb.ServiceTest;
import com.example.mongodb.domain.user.UserService;
import com.example.mongodb.handler.transactional.TransactionTemplateFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class TransactionalTemplateService extends ServiceTest {
    @Autowired
    TransactionTemplateFactoryBean transactionTemplateFactoryBean;
    @Autowired
    UserService userService;
    @Autowired
    MongoTemplate mongoTemplate;
}
