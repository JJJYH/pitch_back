package com.server.pitch.security.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.server.pitch.security.domain.RefreshToken;
import com.server.pitch.users.domain.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.UnsupportedEncodingException;

public interface SecurityService extends UserDetailsService {
    public Users findByEmail(String user_email);
    public Users findById(String user_id);
    public Users createUser(Users user);
    public Users createHrAccount(Users user);
    public Users modifyHrAccount(Users user);

    public void saveToken(String refreshToken, String user_id, String accessToken);

    public void socialLogin(String code);

    public String getAccessToken(String authorizationCode) throws UnsupportedEncodingException;

    public JsonNode getUserResource(String accessToken);

    public String createAccessToken(String user_id);

    public String createRefreshToken(String accessToken, String user_id);

    public boolean checkedAccessTokenValid(String token);
    public boolean checkedRefreshTokenValid(String token);

    public boolean isExpiredToken(String token);

    public String getUserIdFromAccessToken(String token);

    public boolean checkedRefreshTokenByAccessToken(String token);

    public RefreshToken updateRedisHashToken(String token);

    public void logoutByAccessToken(String accessToken);

    public boolean cheackUserByGoogleEmail(String email);

    public String loginGoogleEmail(String email);

}
