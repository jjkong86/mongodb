package com.example.mongodb.service;

import com.example.mongodb.constant.Constant;
import com.example.mongodb.exception.ValidCustomException;
import com.example.mongodb.model.CommonModel;
import com.example.mongodb.model.User;
import com.example.mongodb.repository.UserRepository;
import com.example.mongodb.response.ApiCommonResponse;
import com.example.mongodb.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MongoDBService {

    UserRepository userRepository;

    public MongoDBService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApiCommonResponse saveUser(User user) {
        userRepository.save(user);

        return new ApiCommonResponse();
    }

    public UserResponse getByUserId(Long userId) {
        return UserResponse.builder().user(userRepository.findByUserId(userId)).code(Constant.CODE_SUCCESS.getCode()).build();
    }

    public <T extends Number> UserResponse getByCollectionKey(T userId) {
        return this.getByCollectionKey(User.builder().userId(userId.longValue()).build());
    }

    public <T extends CommonModel<? extends Number>> UserResponse getByCollectionKey(T model) {
        return UserResponse.builder().user(userRepository.findByUserId(model.getCollectionKeyValue().longValue())).code(Constant.CODE_SUCCESS.getCode()).build();
    }

    @Transactional
    public <T extends Number> ApiCommonResponse rollbackTest(T userId, boolean isRollback) {
        userRepository.deleteByUserId(userId.longValue());
        User user = User.builder().userId(userId.longValue()).name("정재공").phoneNumber("010492399").build();
        userRepository.insert(user);

        if (isRollback) {
            throw new ValidCustomException(500, "data rollback");
        }

        //when
        User dbUserData = userRepository.findByUserId(user.getUserId());
        log.info("insert data info : {}", user.toString());
        log.info("db from user data info : {}", dbUserData.toString());
        return new ApiCommonResponse();
    }
}
