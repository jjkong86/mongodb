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
        super.setCreateTime(System.currentTimeMillis());
    }

    @Builder
    public User(Long userId, String name, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public User(User user) {
        super.setCreateTime(user.getCreateTime());
        super.setUpdateTime(user.getUpdateTime());
        this.userId = user.getUserId();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
    }
}
