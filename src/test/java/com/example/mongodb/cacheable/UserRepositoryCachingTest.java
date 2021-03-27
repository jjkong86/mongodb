package com.example.mongodb.cacheable;

import com.example.mongodb.ServiceTest;
import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.repository.UserCacheRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRepositoryCachingTest extends ServiceTest {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(UserRepositoryCachingTest.class);

    @Autowired
    CacheManager cacheManager;

    @Autowired
    UserCacheRepository userCacheRepository;

    @Before
    public void setUp() {
        userCacheRepository.deleteByUserId(99999L);
        userCacheRepository.deleteByUserId(99998L);
        userCacheRepository.save(new User(99999L, "Test", "000-0000-000"));
        userCacheRepository.save(new User(99998L, "Test", "000-0000-000"));
    }

    private Optional<User> getCachedUser(long userId) {
        return ofNullable(cacheManager.getCache("users")).map(c -> c.get(userId, User.class));
    }

    @Test
    public void 캐쉬유저정보_디비유저정보_동등비교() {
        long userId = 99999L;
        Optional<User> userOptional = userCacheRepository.findByUserId(userId);
        logger.info(userOptional.toString());

        userOptional.ifPresent(user -> {
            Optional<User> cachedUserOptional = this.getCachedUser(userId);
            cachedUserOptional.ifPresent(cacheUser -> {
                assertEquals(user.getUserId(), cacheUser.getUserId());
            });
        });
    }

    @Test
    public void 저장되지않는_캐쉬정보_비교() {
        userCacheRepository.findByUserId(0L);

        assertEquals(empty(), getCachedUser(0L));
    }

}
