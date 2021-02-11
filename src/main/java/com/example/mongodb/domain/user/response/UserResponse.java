package com.example.mongodb.domain.user.response;

import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.response.ApiCommonResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponse extends ApiCommonResponse {
    User user;

    @Builder
    public UserResponse(int code, String error, User user) {
        super(code, error);
        this.user = user;
    }
}
