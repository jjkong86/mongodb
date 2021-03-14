package com.example.mongodb.domain.user;

import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.model.UserLog;
import com.example.mongodb.domain.user.repository.UserRepository;
import com.example.mongodb.exception.ValidCustomException;
import com.example.mongodb.utils.DateUtil;
import com.example.mongodb.handler.transactional.TemplateTransactional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserTransactionService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public UserTransactionService(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /*
    * propagation option Propagation.REQUIRES_NEW
    * */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T extends Number> void requiresNewJpa(T userId) {
        User user = User.builder().userId(userId.longValue() + 1).name("정재공").phoneNumber("010492399").build();
        userRepository.save(user);
    }

    /*
     * propagation option Propagation.REQUIRES_NEW
     * */
    @TemplateTransactional(propagation = Propagation.REQUIRES_NEW, timeout = 3)
    public void requiresNewTemplate(User user) {
        User changeUser = new User(user.getUserId() + 1);
        this.dataSaveTemplate(changeUser);
        this.saveUserLogWithTtlIndex(user);
    }

    public <T> void dataSaveTemplate(T data) {
        this.mongoTemplate.save(data, data.getClass().getSimpleName());
    }

    public void saveUserLogWithTtlIndex(User user) {
        UserLog log = new UserLog(user, DateUtil.get1MinuteTime());
        this.dataSaveTemplate(log);
    }

    public <T extends Number> void findUserByUserId(T id) {
        userRepository.findByUserId(id.longValue())
                .orElseThrow(() -> new ValidCustomException(500, "not found user data by userId."));
    }

}
