package com.example.mongodb.user;

import com.example.mongodb.ServiceTest;
import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.repository.UserRepository;
import com.example.mongodb.exception.ValidCustomException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceTest extends ServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void 유저_컬렉션_데이터_삽입() {
        //given
        long userId = 133L;
        userRepository.deleteByUserId(userId);
        User user = User.builder().userId(userId).name("정재공").phoneNumber("010492399").build();
        userRepository.save(user);

        //when
        User dbUserData = userRepository.findByUserId(user.getUserId()).orElseThrow(() -> new ValidCustomException(500, "not found user data by userId."));

        //then
        Assert.assertEquals(user.getUserId(), dbUserData.getUserId());
        Assert.assertEquals(user.getName(), dbUserData.getName());
    }
}
