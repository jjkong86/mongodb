package com.example.mongodb.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class TransactionCallbackWithoutResultWrapper {
    public static TransactionCallbackWithoutResult doInTransactionWithoutResult(CustomCallback callback) {
        return new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NotNull TransactionStatus status) {
                try {
                    callback.action();
                } catch (Exception e) {
                    status.isRollbackOnly();
                }
            }
        };
    }
}

