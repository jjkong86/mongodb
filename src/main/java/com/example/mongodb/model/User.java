package com.example.mongodb.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@ToString
public class User extends CommonModel<Long> {
    @CollectionKey
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
