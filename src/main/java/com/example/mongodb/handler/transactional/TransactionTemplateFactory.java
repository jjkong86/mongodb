package com.example.mongodb.handler.transactional;

import com.example.mongodb.utils.CustomCallback;

public interface TransactionTemplateFactory {
    void execute(CustomCallback callback);
}
