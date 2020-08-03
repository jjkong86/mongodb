package com.example.mongodb;

import com.example.mongodb.model.User;
import com.example.mongodb.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
public class MongoDBTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 유저_컬렉션_데이터_삽입() {
        //given
        User user = User.builder().userId(100000L).name("정재공").phoneNumber("010492399").build();
        userRepository.insert(user);

        //when
        User dbUserData = userRepository.findByUserId(user.getUserId());

        //then
        Assert.assertEquals(user.getUserId(), dbUserData.getUserId());
        Assert.assertEquals(user.getName(), dbUserData.getName());
    }


}
