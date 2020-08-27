package com.example.mongodb;

import com.example.mongodb.exception.ValidCustomException;
import com.example.mongodb.model.User;
import com.example.mongodb.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoDBTest {

    private UserRepository userRepository;

    public MongoDBTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void 유저_컬렉션_데이터_삽입() {
        //given
        long userId = 100000L;
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
