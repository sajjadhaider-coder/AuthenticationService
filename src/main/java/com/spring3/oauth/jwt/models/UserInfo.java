package com.spring3.oauth.jwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @NotNull
    @Column(name = "USER_NAME", unique = true, nullable = false, length = 20)
    @JsonIgnore // Hide sensitive data if necessary
    private String username;

    @NotNull
    @Column(name = "VERIFICATION_CODE", length = 10)
    private String verificationCode;

    @JsonIgnore // Prevent password from being serialized
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "DEVICE_TYPE")
    private String deviceType;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Column(name = "LOCATION")
    private String userLocation;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "CREATED_BY")
    private int createdBy;

    @Column(name = "USER_ID", nullable = false)
    private int userId;

    @Column(name = "UPDATED_AT")
    private LocalDateTime UpdatedAt;

    @Column(name = "UPDATED_BY")
    private int UpdatedBy;

}
