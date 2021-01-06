package com.example.mongodb.response;

import com.example.mongodb.constant.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ApiCommonResponse {
    int code = Constant.CODE_SUCCESS.getCode();
    String error;

    public ApiCommonResponse(int code, String error) {
        if (code == 0)
            code = Constant.CODE_SUCCESS.getCode();

        this.code = code;
        this.error = error;
    }
}
