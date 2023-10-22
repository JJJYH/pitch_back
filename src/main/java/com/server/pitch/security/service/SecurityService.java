package com.server.pitch.security.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.server.pitch.users.domain.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {
    public Users findByEmail(String user_email);
    public Users findById(String user_id);
    public Users createUser(Users user);

    public void saveToken(String refreshToken, String user_id, String accessToken);

    public void socialLogin(String code);

    public String getAccessToken(String authorizationCode);

    public JsonNode getUserResource(String accessToken);

}
