package com.server.pitch.security.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.server.pitch.security.service.SecurityService;
import com.server.pitch.users.domain.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/auth")
public class SecurityController {

    @Autowired
    SecurityService securityService;

    @Autowired
    Environment env;

//    @GetUserAccessToken
//    @GetMapping("/check-session")
//    public ResponseEntity<Users> checkedSession(@RequestParam String token){
//
//    }


    @GetMapping("/login-user")
    public ResponseEntity<Users> getLoginUserInfo(@RequestParam String token){
        log.info(token);
        Users loginUser = null;
        if(securityService.checkedAccessTokenValid(token)) {
            Claims claims = Jwts.parser().setSigningKey("jwtAccess").parseClaimsJws(token).getBody();
            String user_id = claims.getSubject();
            loginUser = securityService.findById(user_id);
            log.info(loginUser.toString());
            loginUser.setUser_pw(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(loginUser);
        }else{
            return null;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutAccount(@RequestHeader(value = "Authorization")String accessToken ){
        log.info(accessToken);
        securityService.logoutByAccessToken(accessToken.substring(7));
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 성공");
    }

    @PostMapping("/create")
    public ResponseEntity<Users> accountCreate(@RequestBody Users user){
        log.info(user.toString());
        securityService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/create-hr")
    public ResponseEntity<Users> hrAccountCreate(@RequestBody Users users){
        log.info("=============등록=============");
        log.info(users.toString());
        users.setUser_pw("1234");
        log.info(users.toString());
        securityService.createHrAccount(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @PutMapping("/update-hr")
    public ResponseEntity<Users> hrAccountModify(@RequestBody Users users){
        log.info("================수정================");
        log.info(users.toString());
        securityService.modifyHrAccount(users);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/google")
    public ResponseEntity<Object> oauthInfo(@RequestParam String code) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        String message = null;
        //1. 유저정보 받아오기
        String accesToken = null;
        String sendAccessToken = securityService.getAccessToken(code);
        JsonNode googleUserInfo = securityService.getUserResource(sendAccessToken);
        log.info(googleUserInfo.toString());
        //2. 유저정보 검증하기
        String socialEmail = googleUserInfo.get("email").asText();
        log.info(socialEmail);
        if(securityService.cheackUserByGoogleEmail(socialEmail)){
            //3. 검증 성공시 유저정보로 로그인하기
            accesToken = securityService.loginGoogleEmail(socialEmail);
            headers.add("accessToken", accesToken);
        }else{
            //4. 검증 실패시 유저정보가 없으면 회원가입으로 유도하기
            message = "Google Email is not registered";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"" + message + "\"}");
        }
        message = "Social Login Successful";
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body("{\"message\": \"" + message + "\"}");
    }

    @GetMapping("/google-login")
    public ResponseEntity<String> googleLogin(){
       log.info("1. 구글 로그인");
        return ResponseEntity.status(HttpStatus.OK).body("구글 로그인");
    }

    @PostMapping("/delete-hr-account")
    public ResponseEntity<String> deleteHrAccount(@RequestParam String user_id){
        log.info("===================삭제==================");
        log.info(user_id);
        securityService.deleteByUserId(user_id);
        return ResponseEntity.status(HttpStatus.OK).body("delete complete");
    }
}
