package com.server.pitch.security.controller;

import com.server.pitch.security.service.SecurityService;
import com.server.pitch.users.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    @Autowired
    SecurityService securityService;

    @PostMapping("/create")
    public ResponseEntity<Users> accountCreate(@RequestBody Users user){
        securityService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/google")
    public void oauthInfo(@RequestParam String code){
        securityService.socialLogin(code);
        //System.out.println(securityService.getAccessToken(code));
        String accessToken = securityService.getAccessToken(code);
        System.out.println(securityService.getUserResource(accessToken));
    }
}
