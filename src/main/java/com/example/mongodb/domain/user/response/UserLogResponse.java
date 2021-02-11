package com.example.mongodb.domain.user.response;

import com.example.mongodb.domain.user.model.UserLog;
import com.example.mongodb.response.ApiCommonResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserLogResponse extends ApiCommonResponse {
    UserLog userLog;

    @Builder
    public UserLogResponse(int code, String error, UserLog userLog) {
        super(code, error);
        this.userLog = userLog;
    }
}
