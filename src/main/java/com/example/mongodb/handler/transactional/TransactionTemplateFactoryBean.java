package com.example.mongodb.handler.transactional;

import com.example.mongodb.utils.CustomCallback;
import com.example.mongodb.utils.TransactionCallbackWithoutResultWrapper;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class TransactionTemplateFactoryBean implements TransactionTemplateFactory {
    private final TransactionTemplate transactionTemplate;

    public TransactionTemplateFactoryBean(MongoTransactionManager mongoTransactionManager, MongoTemplate mongoTemplate) {
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
        this.transactionTemplate = new TransactionTemplate(mongoTransactionManager);
        transactionTemplate.setTimeout(3);
    }

    @Override
    public void execute(CustomCallback callback) {
        transactionTemplate.execute(TransactionCallbackWithoutResultWrapper.doInTransactionWithoutResult(callback));
    }
}
