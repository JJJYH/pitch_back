package com.server.pitch.loginuser.controller;

import com.server.pitch.loginuser.domain.LoginUser;
import com.server.pitch.loginuser.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class LoginUserController {

    @Autowired
    LoginUserService loginUserService;

    @GetMapping("/test")
    public String test(){
        return "testtest";
    }
    @GetMapping("/list")
    public List<LoginUser> list(){
        return loginUserService.list();
    }
}
