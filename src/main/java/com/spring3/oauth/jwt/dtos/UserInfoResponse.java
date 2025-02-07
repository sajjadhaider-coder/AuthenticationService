package com.spring3.oauth.jwt.dtos;

import com.spring3.oauth.jwt.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfoResponse {

    private Long id;
    private String username;
    private String verificationCode;
    private String deviceType;
    private String status;
    private String ipAddress;
    private String userLocation;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private List<UserRole> userRoleList;

}
