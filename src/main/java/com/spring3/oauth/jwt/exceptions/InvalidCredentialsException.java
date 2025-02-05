package com.spring3.oauth.jwt.exceptions;

import com.spring3.oauth.jwt.dtos.ApiResponse;
import com.spring3.oauth.jwt.dtos.JwtResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class InvalidCredentialsException extends RuntimeException {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ApiResponse response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials provided.", null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}