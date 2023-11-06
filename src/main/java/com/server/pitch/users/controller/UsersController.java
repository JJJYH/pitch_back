package com.server.pitch.users.controller;

import com.server.pitch.aop.GetUserAccessToken;
import com.server.pitch.users.domain.Users;
import com.server.pitch.users.service.UsersService;
import com.server.pitch.security.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class UsersController {

    @Autowired
    UsersService usersService;

    @Autowired
    SecurityRepository securityRepository;

    @GetMapping("/test")
    public String test(){
        securityRepository.save("accessKey", "tokenValue");
        securityRepository.findByKey("accessKey");

        return securityRepository.findByKey("accessKey");
    }
    @GetMapping("/list")
    public List<Users> list(){
        return usersService.list();
    }

    @GetUserAccessToken
    @GetMapping("/loginUserTest")
    public void test2(Users loginUser){
        //Users loginUser = (Users) session.getAttribute("loginUser");
        System.out.println(loginUser);
    }

    @PostMapping("/test")
    public String testToken(){
        return "Good";
    }

}
