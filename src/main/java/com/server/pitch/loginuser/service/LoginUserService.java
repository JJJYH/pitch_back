package com.server.pitch.loginuser.service;

import com.server.pitch.loginuser.domain.LoginUser;
import com.server.pitch.loginuser.mapper.LoginUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginUserService {
    @Autowired
    LoginUserMapper loginUserMapper;

    public List<LoginUser> list(){
        return loginUserMapper.list();
    }
}
