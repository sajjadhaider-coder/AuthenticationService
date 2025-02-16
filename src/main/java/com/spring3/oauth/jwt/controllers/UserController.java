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

@CrossOrigin(origins = "http://localhost:8841")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/updateUser")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserInfo userRequest) {
        ApiResponse apiResponse = null;
        int statusCode = 0;
        UserInfo userResponse = null;
        try {
            userResponse = userService.updateUser(userRequest);
            statusCode = HttpStatus.OK.value();
            apiResponse = new ApiResponse(statusCode, "Success", userResponse);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            apiResponse = new ApiResponse(statusCode, "Success", userResponse);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get greeting message", description = "Returns a greeting message")
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("Welcome", HttpStatus.OK);  // 200 OK for a successful request
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO, HttpServletRequest httpServletRequest) {
        Authentication authentication = null;
        JwtResponseDTO jwtResponse = null;

        try {
    authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword())  // Authenticate using account number and password
    );
    if (authentication.isAuthenticated()) {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());  // Create refresh token for the authenticated user
        jwtResponse = JwtResponseDTO.builder()
                .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))  // Generate access token
                .token(refreshToken.getToken()).build();  // Include refresh token in the response
        UserInfo userInfo = userService.getUserByUserName(authRequestDTO.getUsername());

        userService.updateUser(userInfo);
        // return new ResponseEntity<>(jwtResponse, HttpStatus.OK);  // 200 OK for successful authentication

    } else {
        //return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Success", jwtResponse), HttpStatus.OK);
       // throw new InvalidCredentialsException(); // Custom exception for invalid login credentials
    }
} catch (Exception e) {
            jwtResponse.setToken("");
            ApiResponse response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials provided.", jwtResponse);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
}
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Success", jwtResponse), HttpStatus.OK);
    }

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

    @PostMapping("/deleteUser/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") String userId){
        int statusCode = 0;
        ApiResponse response = null;
        Boolean isDeleted = userService.deleteUser(Long.valueOf(userId));
        if (!isDeleted) {
            statusCode = HttpStatus.NOT_FOUND.value();
            response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "FAILED", null);
            throw new UserNotFoundException("User not found.");
        } else {
            statusCode = HttpStatus.OK.value();
            response = new ApiResponse<>(HttpStatus.OK.value(), "Success", null);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
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
