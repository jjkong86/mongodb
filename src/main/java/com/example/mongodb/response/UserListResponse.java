package com.example.mongodb.response;

import com.example.mongodb.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserListResponse extends ApiCommonResponse {
    List<User> users;

    @Builder
    public UserListResponse(int code, String error, List<User> users) {
        super(code, error);
        this.users = users;
    }
}
