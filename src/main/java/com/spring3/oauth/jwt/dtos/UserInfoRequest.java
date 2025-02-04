package com.spring3.oauth.jwt.dtos;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfoRequest {

    private Long id;

    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull
    @Size(min = 8, message = "Confirm password must be at least 8 characters long")
    private String confirmPassword;

    @NotNull
    @Size(min = 10, max = 20, message = "Account number must be between 10 and 20 characters")
    private String username;

    @NotNull
    @Size(min = 6, max = 10, message = "Verification code must be between 6 and 10 characters")
    private String verificationCode;

    private String deviceType;
    private String status;
    private String ipAddress;
    private String userLocation;
    private LocalDateTime createdAt;
    private int createdBy;
    private int userId;
    private LocalDateTime UpdatedAt;
    private int UpdatedBy;

}
