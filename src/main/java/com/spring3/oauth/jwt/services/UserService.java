package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.dtos.UserInfoRequest;
import com.spring3.oauth.jwt.dtos.UserInfoResponse;
import com.spring3.oauth.jwt.models.UserInfo;
import jakarta.servlet.http.HttpServletRequest;


import java.util.List;


public interface UserService {

    UserInfoResponse saveUser(UserInfoRequest userInfoRequest);

    UserInfoResponse getUser();

    List<UserInfoResponse> getAllUser();
     String  returnClientIp(HttpServletRequest request);

    UserInfoResponse updateUser(UserInfo userInfoRequest, HttpServletRequest httpServletRequest);

    UserInfo getUserByUserName(String userName);

}
