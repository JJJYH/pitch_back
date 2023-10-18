package com.server.pitch.security.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.server.pitch.security.domain.RefreshToken;
import com.server.pitch.security.repository.RedisTokenRepository;
import com.server.pitch.users.domain.Users;
import com.server.pitch.users.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
@Slf4j
public class SecurityServiceImpl implements SecurityService{

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisTokenRepository redisTokenRepository;

    @Autowired
    private Environment env;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users_nm = new Users();
        users_nm.setUser_email(username);
        Users user = usersMapper.selectUser(users_nm);
        log.info(user.getUser_nm());

        if (user == null){
            throw new UsernameNotFoundException(username);
        }

        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user.getRole() != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
        }
        User returnUser = new User(user.getUser_email(), user.getUser_pw(),
                true,true,true,true, grantedAuthorities);
        System.out.println(returnUser);

        return returnUser;
    }

    @Override
    public Users findByEmail(String user_email) {
        Users user = new Users();
        user.setUser_email(user_email);
        return usersMapper.selectUser(user);
    }

    @Override
    public Users findById(String user_id){
        Users user = new Users();
        user.setUser_id(user_id);
        return usersMapper.selectUser(user);
    }

    @Override
    public Users createUser(Users user) {
        user.setUser_pw(passwordEncoder.encode((user.getUser_pw())));
        usersMapper.insertUser(user);
        return user;
    }

    @Override
    public void socialLogin(String code) {
        System.out.println("code = "+code);
        //System.out.println("registrationID = "+ registrationID);
    }

    @Override
    public String getAccessToken(String authorizationCode) {
        String clientId = env.getProperty("oauth2.google.client-id");
        String clientSecret = env.getProperty("oauth2.google.client-secret");
        String redirectUri = env.getProperty("oauth2.google.redirect-uri");
        String tokenUri = env.getProperty("oauth2.google.token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("clientId", clientId);
        params.add("clientSecret", clientSecret);
        params.add("redirectUri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        System.out.println("1");
        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        System.out.println(accessTokenNode);
        return accessTokenNode.get("access_token").asText();
    }

    @Override
    public JsonNode getUserResource(String accessToken) {
        String authUri = env.getProperty("oauth2.google.auth-uri");

        System.out.println(accessToken);
        HttpHeaders headers = new HttpHeaders();
        System.out.println(accessToken);
        headers.set("Authorization", "Bearer "+accessToken);
        HttpEntity entity = new HttpEntity(headers);
        System.out.println(headers);
        return restTemplate.exchange(authUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

    @Transactional
    public void saveToken(String refreshToken, String user_id, String accessToken){
        redisTokenRepository.save(new RefreshToken(refreshToken, user_id, accessToken));
    }



}
