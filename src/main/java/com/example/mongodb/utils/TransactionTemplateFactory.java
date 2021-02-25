package com.example.mongodb.utils;

import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class TransactionTemplateFactory {
    private final TransactionTemplate transactionTemplate;

    public TransactionTemplateFactory(MongoTransactionManager mongoTransactionManager, MongoTemplate mongoTemplate) {
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
        this.transactionTemplate = new TransactionTemplate(mongoTransactionManager);
        transactionTemplate.setTimeout(3);
    }

    public void execute(CustomCallback callback) {
        transactionTemplate.execute(TransactionCallbackWithoutResultWrapper.doInTransactionWithoutResult(callback));
    }
}
