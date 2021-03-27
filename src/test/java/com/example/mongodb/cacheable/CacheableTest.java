package com.example.mongodb.cacheable;

import com.example.mongodb.ServiceTest;
import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.repository.UserCacheRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CacheableTest extends ServiceTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    UserCacheRepository repository;

    @BeforeEach
    void setUp() {
        repository.save(new User(99999L, "Test", "000-0000-000"));
        repository.save(new User(99998L, "Test", "000-0000-000"));
    }

    private Optional<User> getCachedBook(long userId) {
        return ofNullable(cacheManager.getCache("users")).map(c -> c.get(userId, User.class));
    }

    @Test
    public void getUserByCache() {
        long userId = 99999L;
        Optional<User> user = repository.findByUserId(userId);

        assertEquals(user, this.getCachedBook(userId));
    }

}
