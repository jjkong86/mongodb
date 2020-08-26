package com.example.mongodb.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@ToString
public class User extends CommonModel<Long> {
    @CollectionKey
    @Indexed(unique = true)
    private Long userId;
    private String name;
    private String phoneNumber;

    User() {
        super.createTime = System.currentTimeMillis();
    }

    @Builder
    public User(Long userId, String name, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
