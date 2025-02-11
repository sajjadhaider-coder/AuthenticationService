package com.spring3.oauth.jwt.helpers;


import com.spring3.oauth.jwt.models.UserInfo;
import com.spring3.oauth.jwt.models.UserRole;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

public class CustomUserDetails extends UserInfo implements UserDetails {

    private String username;
    private String password;
    private final RestTemplate restTemplate = new RestTemplate();
    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo byUsername) {
        this.username = byUsername.getUsername();
        this.password= byUsername.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();
        UserInfo userInfo = this.getUserRolesByUserName(byUsername.getUsername());
        for(UserRole role : userInfo.getRoles()){
             auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }
        this.authorities = auths;
    }

    public UserInfo getUserRolesByUserName(String userName) {
        String url = "http://localhost:8083/api/v1/user/getUserRolesByUserName/{username}";
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("username", userName);

        ResponseEntity<UserInfo> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null, // No request body needed for GET
                UserInfo.class,
                uriVariables
        );
        return response.getBody();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
