package com.example.mongodb.repository;

import com.example.mongodb.model.UserLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserLogRepository extends MongoRepository<UserLog, Long> {

}
