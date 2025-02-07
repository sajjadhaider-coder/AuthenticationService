package com.spring3.oauth.jwt.controllers;

import com.spring3.oauth.jwt.dtos.ApiResponse;
import com.spring3.oauth.jwt.models.UserRole;
import com.spring3.oauth.jwt.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8841")
@RequestMapping("/api/v1/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @PostMapping("/addRole")
    public ResponseEntity<ApiResponse> saveRole(@RequestBody UserRole userRole) {
        int statusCode = 0;
        ApiResponse apiResponse = null;

        try {
           userRole = roleService.addRole(userRole);
           statusCode = HttpStatus.OK.value();
           apiResponse = new ApiResponse(statusCode, "Success", userRole);
        } catch (Exception e) {
            statusCode = HttpStatus.UNAUTHORIZED.value();
            apiResponse = new ApiResponse(statusCode, "Error", userRole);
        }
        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(statusCode));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/getRoles")
    public ResponseEntity<ApiResponse> getRoles() {
        int statusCode = 0;
        ApiResponse apiResponse = null;
        List<UserRole> uList = new ArrayList<>();

        try {
            uList = roleService.getRoles();
            statusCode = HttpStatus.OK.value();
            apiResponse = new ApiResponse(statusCode, "Success", uList);
        } catch (Exception e) {
            statusCode = HttpStatus.UNAUTHORIZED.value();
            apiResponse = new ApiResponse(statusCode, "Error", uList);
        }
        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(statusCode));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/getRoleById/{id}")
    public ResponseEntity<ApiResponse> getRoleById(@PathVariable("id") Long id) {
        int statusCode = 0;
        ApiResponse apiResponse = null;
        Optional<UserRole> uRole = Optional.of(new UserRole());

        try {
            uRole = roleService.getRoleById(id);
            statusCode = HttpStatus.OK.value();
            apiResponse = new ApiResponse(statusCode, "Success", uRole);
        } catch (Exception e) {
            statusCode = HttpStatus.UNAUTHORIZED.value();
            apiResponse = new ApiResponse(statusCode, "Error", uRole);
        }
        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(statusCode));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/testAuth")
    public ResponseEntity<ApiResponse> getRoleById() {
        int statusCode = 0;
        ApiResponse apiResponse = null;

        try {
            statusCode = HttpStatus.OK.value();
            apiResponse = new ApiResponse(statusCode, "Success: your role is user because this api is only authorized to users", null);
        } catch (Exception e) {
            statusCode = HttpStatus.UNAUTHORIZED.value();
            apiResponse = new ApiResponse(statusCode, "Error: you are not authorized to access this API, please contact admin to change your role", null);
        }
        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(statusCode));
    }
}
