package com.example.mongodb.domain.user.repository;

import com.example.mongodb.domain.user.model.UserLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserLogRepository extends MongoRepository<UserLog, Long> {

}
