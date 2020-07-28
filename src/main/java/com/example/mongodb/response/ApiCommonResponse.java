package com.example.mongodb.response;

import com.example.mongodb.constant.Constant;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class ApiCommonResponse {
	int code = Constant.CODE_SUCCESS.getCode();
	String error;

	@Builder
	public ApiCommonResponse(int code, String error) {
		this.code = code;
		this.error = error;
	}
}
