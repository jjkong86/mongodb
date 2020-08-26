package com.example.mongodb.service;

import com.example.mongodb.constant.Constant;
import com.example.mongodb.exception.ValidCustomException;
import com.example.mongodb.model.CommonModel;
import com.example.mongodb.model.User;
import com.example.mongodb.repository.UserRepository;
import com.example.mongodb.response.ApiCommonResponse;
import com.example.mongodb.response.UserListResponse;
import com.example.mongodb.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class MongoDBService {

    UserRepository userRepository;

    public MongoDBService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse saveUser(User user) {
        User userData = Optional.ofNullable(this.saveUserHandling(user))
                .orElseThrow(() -> new ValidCustomException(201, "duplicate key error collection"));

        return UserResponse.builder().user(userData).code(Constant.CODE_SUCCESS.getCode()).build();
    }

    public User saveUserHandling(User user) {
        try {
            return userRepository.save(user);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public UserListResponse findUserList() {
        return UserListResponse.builder().users(userRepository.findAll()).build();
    }

    public <T extends Number> UserResponse getByCollectionKey(T userId) {
        return this.getByCollectionKey(User.builder().userId(userId.longValue()).build());
    }

    public <T extends CommonModel<? extends Number>> UserResponse getByCollectionKey(T model) {
        User user = Optional.ofNullable(userRepository.findByUserId(model.getCollectionKeyValue().longValue()))
                .orElseThrow(() -> new ValidCustomException(202, "not found user data by userId."));

        return UserResponse.builder().user(user).code(Constant.CODE_SUCCESS.getCode()).build();
    }

    public UserResponse updateUserByCollectionKey(User user) {
        User userData = Optional.ofNullable(userRepository.findByUserId(user.getUserId()))
                .orElseThrow(() -> new ValidCustomException(500, "not found user data by user id."));

        user.setValueById(userData.getId()); // set user id

        return UserResponse.builder().user(userRepository.save(user)).code(Constant.CODE_SUCCESS.getCode()).build();
    }

    @Transactional
    public <T extends Number> ApiCommonResponse rollbackTest(T userId, boolean isRollback) {
        userRepository.deleteByUserId(userId.longValue());
        User user = User.builder().userId(userId.longValue()).name("정재공").phoneNumber("010492399").build();
        userRepository.save(user);

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
