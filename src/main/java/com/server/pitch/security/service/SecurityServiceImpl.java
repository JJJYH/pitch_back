package com.server.pitch.security.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.server.pitch.security.domain.RefreshToken;
import com.server.pitch.security.repository.RedisTokenRepository;
import com.server.pitch.users.domain.Users;
import com.server.pitch.users.mapper.UsersMapper;
import io.jsonwebtoken.*;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

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
        Users user = new Users();
        try {
             user = findByEmail(username);
             //NullPointerException 유도
            log.info(user.getUser_nm());
        }catch (NullPointerException e){
            user = findById(username);
        }
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
    public Users createHrAccount(Users user) {
        user.setUser_pw(passwordEncoder.encode((user.getUser_pw())));
        if(user.getDepartment().getDept_name()!=null) {
            user.setDepartment(usersMapper.selectDept(user.getDepartment().getDept_name()));
        }
        usersMapper.insertUser(user);
        return null;
    }

    @Override
    public String createAccessToken(String user_id) {
        return Jwts.builder()
                .setSubject(user_id)
                .setExpiration(new Date(System.currentTimeMillis()+
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.asecret"))
                .compact();
    }

    @Override
    public String createRefreshToken(String accessToken, String user_id) {
        return Jwts.builder()
                .setSubject(accessToken)
                .setExpiration(new Date(System.currentTimeMillis()+
                        Long.parseLong(env.getProperty("token.refreshToken_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.rsecret"))
                .compact();
    }

    @Override
    public boolean checkedAccessTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey("jwtAccess").parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            //log.error("Invalid JWT Signature: {}", e.getMessage());
        }catch (MalformedJwtException e){
            //log.error("Invalid Access Token: {}", e.getMessage());
        }catch (ExpiredJwtException e){
            //log.error("Access Token is Expired: {}", e.getMessage());
        }catch (UnsupportedJwtException e){
            //log.error("This Token is unsupported: {}", e.getMessage());
        }catch (IllegalArgumentException e){
            //log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean checkedRefreshTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey("jwtRefresh").parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            log.error("Invalid JWT Signature: {}", e.getMessage());
        }catch (MalformedJwtException e){
            log.error("Invalid Refresh Token: {}", e.getMessage());
        }catch (ExpiredJwtException e){
            log.error("Access Token is Expired: {}", e.getMessage());
        }catch (UnsupportedJwtException e){
            log.error("This Token is unsupported: {}", e.getMessage());
        }catch (IllegalArgumentException e){
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public String getUserIdFromAccessToken(String token) {
        Claims claims = Jwts.parser().setSigningKey("jwtAccess").parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    @Override
    public boolean checkedRefreshTokenByAccessToken(String token) {
        String refreshToken = null;
        try {
             refreshToken = redisTokenRepository.findByAccessToken(token).getRefreshToken();
        }catch (NullPointerException e){
            log.info("refreshToken is null");
        }
        log.info(refreshToken);
        if (refreshToken!=null){
            return checkedRefreshTokenValid(refreshToken);
        }else {
            log.error("RefreshToken is not valid");
            return false;
        }
    }

    @Override
    public RefreshToken updateRedisHashToken(String accessToken) {
        String newAccessToken = null;
        RefreshToken redisToken = redisTokenRepository.findByAccessToken(accessToken);
        newAccessToken = createAccessToken(redisToken.getUser_id());
        redisToken.setAccessToken(newAccessToken);
        redisTokenRepository.save(redisToken);
        return redisToken;
    }

    @Override
    public boolean isExpiredToken(String token) {
        return false;
    }

    @Override
    public void socialLogin(String code) {
        log.info("code = "+code);
        //System.out.println("registrationID = "+ registrationID);
    }

    @Override
    public String getAccessToken(String authorizationCode) throws UnsupportedEncodingException {
        String clientId = env.getProperty("oauth2.google.client-id");
        String clientSecret = env.getProperty("oauth2.google.client-secret");
        String redirectUri = env.getProperty("oauth2.google.redirect-uri");
        String tokenUri = env.getProperty("oauth2.google.token-uri");
        authorizationCode = URLDecoder.decode(authorizationCode, StandardCharsets.UTF_8.name());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        log.info(clientId);
        log.info(clientSecret);
        log.info(tokenUri);
        params.add("code", authorizationCode);
        params.add("clientId", clientId);
        params.add("clientSecret", clientSecret);
        params.add("redirectUri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        log.info("1");
        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        log.info(accessTokenNode.toString());
        return accessTokenNode.get("access_token").asText();
    }

    @Override
    public JsonNode getUserResource(String accessToken) {
        String authUri = env.getProperty("oauth2.google.auth-uri");

        log.info("1."+accessToken);
        HttpHeaders headers = new HttpHeaders();
        log.info("2."+accessToken);
        headers.set("Authorization", "Bearer "+accessToken);
        HttpEntity entity = new HttpEntity(headers);
        log.info("3."+headers.toString());
        return restTemplate.exchange(authUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

    @Override
    public void logoutByAccessToken(String accessToken) {
        RefreshToken entity = redisTokenRepository.findByAccessToken(accessToken);
        log.info(entity.toString());
        redisTokenRepository.delete(entity);
    }

    @Transactional
    public void saveToken(String refreshToken, String user_id, String accessToken){
        redisTokenRepository.save(new RefreshToken(refreshToken, user_id, accessToken));
    }

    @Override
    public boolean cheackUserByGoogleEmail(String email) {
        Users users = findByEmail(email);
        return users != null;
    }

    @Override
    public String loginGoogleEmail(String email) {
        Users user = findByEmail(email);
        log.info(user.getUser_id());
        log.info(user.getUser_nm());

        if(Objects.equals(user.getStatus(), "app")) {
            String accessToken = Jwts.builder()
                    .setSubject(user.getUser_id())
                    .setExpiration(new Date(System.currentTimeMillis() +
                            Long.parseLong(env.getProperty("token.expiration_time"))))
                    .signWith(SignatureAlgorithm.HS512, env.getProperty("token.asecret"))
                    .compact();

            String refreshToken = Jwts.builder()
                    .setSubject(accessToken)
                    .setExpiration(new Date(System.currentTimeMillis() +
                            Long.parseLong(env.getProperty("token.refreshToken_time"))))
                    .signWith(SignatureAlgorithm.HS512, env.getProperty("token.rsecret"))
                    .compact();

            log.info(accessToken);
            log.info(refreshToken);

            saveToken(refreshToken, user.getUser_id(), accessToken);
            //securityRepository.save(accessToken, refreshToken);
            return accessToken;
        }
        return "error";

    }

    @Override
    public Users modifyHrAccount(Users user) {
        log.info("1"+user.toString());
        if(user.getDepartment().getDept_name()!=null) {
            user.setDepartment(usersMapper.selectDept(user.getDepartment().getDept_name()));
        }
        log.info("2"+user.toString());
        usersMapper.updateHr(user);
        return user;
    }

    @Override
    public void deleteByUserId(String user_id) {
        usersMapper.deleteUser(user_id);
    }
}
