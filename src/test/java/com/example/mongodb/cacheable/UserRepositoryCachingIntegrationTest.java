package com.example.mongodb.cacheable;

import com.example.mongodb.ServiceTest;
import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.repository.UserCacheRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.util.AopTestUtils;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserRepositoryCachingIntegrationTest extends ServiceTest {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(UserRepositoryCachingTest.class);

    private static final long userId = 99999L;
    private static final long notExistUserId = 0L;
    private static final User user = new User(userId, "Test", "000-0000-000");
    private static final User notExistUser = new User(notExistUserId, "Test", "000-0000-000");

    private UserCacheRepository mock;

    @Autowired
    private UserCacheRepository userCacheRepository;

    @EnableCaching
    @Configuration
    public static class CachingTestConfig {

        @Bean
        public UserCacheRepository userRepositoryMockImplementation() {
            return mock(UserCacheRepository.class);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("users");
        }
    }

    @Before
    public void setUp() {
        mock = AopTestUtils.getTargetObject(userCacheRepository);

        reset(mock);

        when(mock.findByUserId(eq(0L)))
                .thenReturn(of(notExistUser));

        when(mock.findByUserId(eq(userId)))
                .thenReturn(of(user))
                .thenThrow(new RuntimeException("User should be cached!"));
    }

    @Test
    public void givenCacheUser_whenFindByUserId_thenRepositoryShouldNotBeHit() {
        logger.info(user.toString());
        assertEquals(of(user), userCacheRepository.findByUserId(userId));
        verify(mock).findByUserId(userId);

        assertEquals(of(user), userCacheRepository.findByUserId(userId));
        assertEquals(of(user), userCacheRepository.findByUserId(userId));

        verifyNoMoreInteractions(mock);
    }

    @Test
    public void givenNotCacheUser_whenFindByUserId_thenRepositoryShouldBeHit() {
        assertEquals(of(notExistUser), userCacheRepository.findByUserId(notExistUserId));
        assertEquals(of(notExistUser), userCacheRepository.findByUserId(notExistUserId));
        assertEquals(of(notExistUser), userCacheRepository.findByUserId(notExistUserId));

        verify(mock, times(3)).findByUserId(notExistUserId);

    }

}
