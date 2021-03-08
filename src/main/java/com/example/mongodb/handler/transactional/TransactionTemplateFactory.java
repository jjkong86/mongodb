package com.example.mongodb.handler.transactional;

public interface TransactionTemplateFactory {
    void execute(CustomCallback callback);
}
