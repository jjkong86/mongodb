package com.example.mongodb.domain.user.model;

import com.example.mongodb.model.CollectionKey;
import com.example.mongodb.model.CommonModel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@ToString
@Document(collection = "User")
public class User extends CommonModel<Long> {
    @CollectionKey
    @Indexed(unique = true)
    private Long userId;
    private String name;
    private String phoneNumber;

    public User() {
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

    public User(long userId) {
        this.userId = userId;
    }
}
