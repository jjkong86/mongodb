package com.example.mongodb.model;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class User {
    private Long userId;
    private String name;
    private String phoneNumber;

    @Builder
    public User(Long userId, String name, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
