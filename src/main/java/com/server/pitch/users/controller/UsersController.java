package com.server.pitch.users.controller;

import com.server.pitch.aop.GetUserAccessToken;
import com.server.pitch.users.domain.Users;
import com.server.pitch.users.service.UsersService;
import com.server.pitch.security.repository.SecurityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Slf4j
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
        log.info("사용자 정보 : "+loginUser.toString());
    }

    @PostMapping("/test")
    public String testToken(){
        return "Good";
    }

    @GetMapping("/user-list")
    public ResponseEntity<List<Users>> getUserList(){
        log.info("------------------user list call-------------------");
        return ResponseEntity.status(HttpStatus.OK).body(usersService.list());
    }

    @GetMapping("/hr-list")
    public ResponseEntity<List<Users>> getHRList(){
        log.info("------------------hr list call-------------------");
        return ResponseEntity.status(HttpStatus.OK).body(usersService.hrList());
    }

    @GetMapping("/none-app-hr-list")
    public ResponseEntity<List<Users>> getNoneAppHRList(){
        log.info("------------------hr list call-------------------");
        return ResponseEntity.status(HttpStatus.OK).body(usersService.noneAppHrList());
    }

    @PutMapping("/app-hr")
    public ResponseEntity<String> appHr(@RequestBody List<String> selectedHR){
        log.info("------------------------approval HR------------------------");
        log.info(selectedHR.toString());
        usersService.appHr(selectedHR);
        return ResponseEntity.status(HttpStatus.OK).body("update complete");
    }

    @PostMapping("/rej-hr")
    public ResponseEntity<String> rejHr(@RequestBody List<String> selectedHR){
        log.info("------------------------reject HR-----------------------");
        usersService.deleteByUserIdList(selectedHR);
        return ResponseEntity.status(HttpStatus.OK).body("reject Complete");
    }

}
