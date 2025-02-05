package com.spring3.oauth.jwt.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    private int code;    // HTTP status or custom code
    private String msg;  // Message (could be success or error message)
    private T data;      // Generic data (could be any object)

    // Constructor for success response
    public ApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
