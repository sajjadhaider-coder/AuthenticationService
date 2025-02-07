package com.spring3.oauth.jwt.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sajjadhaider
 * Created By sajjadhaider on 03-02-2025
 * @project oauth-jwt
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDTO {
    private String token;
}
