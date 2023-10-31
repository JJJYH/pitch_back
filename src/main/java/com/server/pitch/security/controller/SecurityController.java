package com.server.pitch.security.controller;

import com.server.pitch.aop.GetUserAccessToken;
import com.server.pitch.security.service.SecurityService;
import com.server.pitch.users.domain.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/auth")
public class SecurityController {

    @Autowired
    SecurityService securityService;

//    @GetUserAccessToken
//    @GetMapping("/check-session")
//    public ResponseEntity<Users> checkedSession(@RequestParam String token){
//
//    }


    @GetMapping("/login-user")
    public ResponseEntity<Users> getLoginUserInfo(@RequestParam String token){
        log.info(token);

        Claims claims = Jwts.parser().setSigningKey("jwtAccess").parseClaimsJws(token).getBody();
        String user_id = claims.getSubject();
        Users loginUser = securityService.findById(user_id);
        log.info(loginUser.toString());
        loginUser.setUser_pw(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutAccount(@RequestHeader(value = "Authorization")String accessToken ){
        log.info(accessToken);
        securityService.logoutByAccessToken(accessToken.substring(7));
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 성공");
    }

    @PostMapping("/create")
    public ResponseEntity<Users> accountCreate(@RequestBody Users user){
        securityService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/google")
    public void oauthInfo(@RequestParam String code) {
        securityService.socialLogin(code);
        //System.out.println(securityService.getAccessToken(code));
        String accessToken = securityService.getAccessToken(code);
        System.out.println(securityService.getUserResource(accessToken));
    }
}
