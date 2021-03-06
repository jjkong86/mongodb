package com.example.mongodb.handler.transactional;

import com.example.mongodb.utils.CustomCallback;
import com.example.mongodb.utils.TransactionCallbackWithoutResultWrapper;
import org.springframework.transaction.support.TransactionTemplate;

public class TemporaryTransactionTemplateFactory implements TransactionTemplateFactory {
    private final TransactionTemplate transactionTemplate;

    public TemporaryTransactionTemplateFactory(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void execute(CustomCallback callback) {
        transactionTemplate.execute(TransactionCallbackWithoutResultWrapper.doInTransactionWithoutResult(callback));
    }
}
