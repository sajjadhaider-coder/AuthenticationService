package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.models.RefreshToken;
import com.spring3.oauth.jwt.models.UserInfo;
import com.spring3.oauth.jwt.repositories.RefreshTokenRepository;
import com.spring3.oauth.jwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * @author sajjadhaider
 * Created By sajjadhaider on 03-02-2025
 * @project oauth-jwt
 */

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){

        UserInfo userInfo = userRepository.findByUsername(username);
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserInfo(userInfo);

        if (existingToken.isPresent()) {
            // If an existing token is found, update its token and expiry date
            existingToken.get().setToken(UUID.randomUUID().toString());
            existingToken.get().setExpiryDate(Instant.now().plusMillis(900000000));
            return refreshTokenRepository.save(existingToken.get());
        } else {
            // If no existing token is found, create a new one
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .userInfo(userInfo)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(900000000))
                    .build();
            return refreshTokenRepository.save(newRefreshToken);
        }
        /*RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userRepository.findByUsername(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(900000000))
                .build();
        return refreshTokenRepository.save(refreshToken);
        */
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;

    }

}
