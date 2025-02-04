package com.spring3.oauth.jwt.controllers;

import com.spring3.oauth.jwt.dtos.*;
import com.spring3.oauth.jwt.exceptions.InvalidCredentialsException;
import com.spring3.oauth.jwt.exceptions.UserNotFoundException;
import com.spring3.oauth.jwt.models.RefreshToken;
import com.spring3.oauth.jwt.models.UserInfo;
import com.spring3.oauth.jwt.services.JwtService;
import com.spring3.oauth.jwt.services.RefreshTokenService;
import com.spring3.oauth.jwt.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;  // Inject UserService
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private JwtService jwtService;  // Inject JwtService for token generation

    @Autowired
    private RefreshTokenService refreshTokenService;  // Inject RefreshTokenService for token management

    @Autowired
    private AuthenticationManager authenticationManager;  // Inject AuthenticationManager for user authentication

    // Register a new user | 注册新用户
    @PostMapping(value = "/signup")
    public ResponseEntity<UserInfoResponse> saveUser(@RequestBody UserInfoRequest userRequest) {
        try {
            UserInfoResponse userResponse = userService.saveUser(userRequest);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);  // 201 Created for successful user creation
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 Internal Server Error for any unexpected exceptions 误
        }
    }

    // Get the list of all users | 获取所有用户列表
    @GetMapping("/users")
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        List<UserInfoResponse> userResponses = userService.getAllUser();
        if (userResponses.isEmpty()) {
            throw new UserNotFoundException("No users found.");  // Custom exception when no users are found
        }
        return new ResponseEntity<>(userResponses, HttpStatus.OK);  // 200 OK for successful response
    }

    // Get profile information of the current user (Requires ROLE_ADMIN)
    @GetMapping("/profile")
    public ResponseEntity<UserInfoResponse> getUserProfile() {
        UserInfoResponse userResponse = userService.getUser();
        if (userResponse == null) {
            throw new UserNotFoundException("User not found.");  // Custom exception for user not found
        }
        return new ResponseEntity<>(userResponse, HttpStatus.OK);  // 200 OK for successful profile retrieval
    }

    // Example endpoint to return a welcome message (Requires ROLE_ADMIN)
    @Operation(summary = "Get greeting message", description = "Returns a greeting message")
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("Welcome", HttpStatus.OK);  // 200 OK for a successful request
    }

    // Authenticate user and generate JWT access token
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO, HttpServletRequest httpServletRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword())  // Authenticate using account number and password
        );

        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());  // Create refresh token for the authenticated user
            JwtResponseDTO jwtResponse = JwtResponseDTO.builder()
                    .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))  // Generate access token
                    .token(refreshToken.getToken()).build();  // Include refresh token in the response
            UserInfo userInfo = userService.getUserByUserName(authRequestDTO.getUsername());

            userService.updateUser(userInfo, httpServletRequest);
            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);  // 200 OK for successful authentication
        } else {
            throw new InvalidCredentialsException("Invalid credentials provided.");  // Custom exception for invalid login credentials
        }
    }

    // Refresh the access token using the refresh token | 使用刷新令牌刷新访问令牌
    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        JwtResponseDTO jwtResponse = refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)  // Verify the refresh token expiration
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());  // Generate new access token for the user
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();  // Return the same refresh token with the new access token
                }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB."));  // Throw exception if refresh token is not found

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);  // 200 OK for successful token refresh
    }

    public Claims validateToken(String token, String secretKey) {
        try {
            // Parse the token and validate its signature
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes()) // Use the same secret key used to sign the token
                    .build()
                    .parseClaimsJws(token) // Throws an exception if invalid
                    .getBody();
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT signature");
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }
}
