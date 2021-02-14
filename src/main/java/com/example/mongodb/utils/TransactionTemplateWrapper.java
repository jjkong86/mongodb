package com.example.mongodb.utils;

import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class TransactionTemplateWrapper {
    private final MongoTransactionManager mongoTransactionManager;
    private final MongoTemplate mongoTemplate;

    public TransactionTemplateWrapper(MongoTransactionManager mongoTransactionManager, MongoTemplate mongoTemplate) {
        this.mongoTransactionManager = mongoTransactionManager;
        this.mongoTemplate = mongoTemplate;
    }

    public void execute(CustomCallback callback) {
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
        TransactionTemplate transactionTemplate = new TransactionTemplate(mongoTransactionManager);
        transactionTemplate.execute(TransactionCallbackWithoutResultWrapper.doInTransactionWithoutResult(callback));
    }
}
