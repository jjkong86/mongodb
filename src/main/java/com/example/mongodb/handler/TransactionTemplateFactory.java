package com.example.mongodb.handler;

import com.example.mongodb.utils.CustomCallback;
import com.example.mongodb.utils.TransactionCallbackWithoutResultWrapper;
import org.springframework.transaction.support.TransactionTemplate;

public class TransactionTemplateFactory {
    private final TransactionTemplate transactionTemplate;

    public TransactionTemplateFactory(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public void execute(CustomCallback callback) {
        transactionTemplate.execute(TransactionCallbackWithoutResultWrapper.doInTransactionWithoutResult(callback));
    }
}
