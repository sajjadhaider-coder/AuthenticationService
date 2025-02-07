package com.spring3.oauth.jwt.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sajjadhaider
 * Created By sajjadhaider on 03-02-2025
 * @project oauth-jwt
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDTO implements Serializable {

    private String accessToken;
    private String token;
}
