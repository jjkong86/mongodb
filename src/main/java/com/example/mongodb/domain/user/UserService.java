package com.example.mongodb.domain.user;

import com.example.mongodb.constant.Constant;
import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.model.UserLog;
import com.example.mongodb.domain.user.repository.UserLogRepository;
import com.example.mongodb.domain.user.repository.UserRepository;
import com.example.mongodb.domain.user.response.UserListResponse;
import com.example.mongodb.domain.user.response.UserLogResponse;
import com.example.mongodb.domain.user.response.UserResponse;
import com.example.mongodb.exception.ValidCustomException;
import com.example.mongodb.model.CommonModel;
import com.example.mongodb.response.ApiCommonResponse;
import com.example.mongodb.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserLogRepository userLogRepository;
    private final UserTransactionService userTransactionService;

    public UserService(UserRepository userRepository, UserLogRepository userLogRepository, UserTransactionService userTransactionService) {
        this.userRepository = userRepository;
        this.userLogRepository = userLogRepository;
        this.userTransactionService = userTransactionService;
    }

    public UserResponse saveUser(User user) {
        User userData = this.saveUserHandling(user);
        return UserResponse.builder().user(userData).code(Constant.CODE_SUCCESS.getCode()).build();
    }

    public User saveUserHandling(User user) {
        try {
            return userRepository.save(user); // with out _class field
        } catch (RuntimeException e) {
            throw new ValidCustomException(201, "duplicate key error collection.");
        }
    }

    public UserListResponse findUserList() {
        return UserListResponse.builder().users(userRepository.findAll()).build();
    }

    public <T extends Number> UserResponse getByCollectionKey(T userId) {
        return this.getByCollectionKey(User.builder().userId(userId.longValue()).build());
    }

    public <T extends CommonModel<? extends Number>> UserResponse getByCollectionKey(T model) {
        User user = this.findUserByUserId(model.getCollectionKeyValue().longValue());
        return UserResponse.builder().user(user).code(Constant.CODE_SUCCESS.getCode()).build();
    }

    @Cacheable(cacheNames="user",key="#userId")
    public <T extends Number> User getUserByUserId(T userId) {
        return this.findUserByUserId(userId);
    }


    public UserResponse updateUserByCollectionKey(User user) {
        // optional
        User userData = this.findUserByUserId(user.getUserId());
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
        User dbUserData = this.findUserByUserId(user.getUserId());
        log.info("insert data info : {}", user.toString());
        log.info("db from user data info : {}", dbUserData.toString());
        return new ApiCommonResponse();
    }

    @Transactional
    public <T extends Number> ApiCommonResponse requiresNewTest(T userId, boolean isRollback) {
        userRepository.deleteByUserId(userId.longValue());
        User user = User.builder().userId(userId.longValue()).name("정재공").phoneNumber("010492399").build();
        userRepository.save(user);
        userTransactionService.requiresNewJpa(userId);

        if (isRollback) {
            throw new ValidCustomException(500, "data rollback");
        }

        //when
        User dbUserData = this.findUserByUserId(user.getUserId());
        log.info("insert data info : {}", user.toString());
        log.info("db from user data info : {}", dbUserData.toString());
        return new ApiCommonResponse();
    }



    public <T extends Number> User findUserByUserId(T id) {
        return userRepository.findByUserId(id.longValue())
                .orElseThrow(() -> new ValidCustomException(500, "not found user data by userId."));
    }

    public ApiCommonResponse saveUserLogWithTtlIndex(Long userId) {
        UserLog log = new UserLog(this.findUserByUserId(userId), DateUtil.get1MinuteTime());
        userLogRepository.save(log);
        return UserLogResponse.builder().userLog(log).build();
    }

    public void saveUserLogWithTtlIndex(User user) {
        UserLog log = new UserLog(user, DateUtil.get1MinuteTime());
        userLogRepository.save(log);
    }
}
