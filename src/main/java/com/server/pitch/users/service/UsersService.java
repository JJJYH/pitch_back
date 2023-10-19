package com.server.pitch.users.service;

import com.server.pitch.users.domain.Users;
import com.server.pitch.users.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    @Autowired
    UsersMapper usersMapper;

    public Users findById(String id){
        return new Users();
    }

    public List<Users> list(){
        return usersMapper.list();
    }
}
