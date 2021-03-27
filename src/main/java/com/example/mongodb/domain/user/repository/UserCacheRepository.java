package com.example.mongodb.domain.user.repository;


import com.example.mongodb.domain.user.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserCacheRepository extends MongoRepository<User, Long> {

//    note the SpEL expression “#a0” instead of the more readable “#userId”.
//    We do this because the proxy won't keep the parameter names. So, we use the alternative #root.arg[0], p0 or a0 notation.
    @Cacheable(value = "users", unless = "#a0==0L")
    Optional<User> findByUserId(Long userId);

    Optional<User> deleteByUserId(Long userId);
}
