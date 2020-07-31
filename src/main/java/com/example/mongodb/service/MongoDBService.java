package com.example.mongodb.service;

import com.example.mongodb.constant.Constant;
import com.example.mongodb.model.User;
import com.example.mongodb.repository.UserRepository;
import com.example.mongodb.response.ApiCommonResponse;
import com.example.mongodb.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
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
}
