package com.server.pitch.users.service;

import com.server.pitch.users.domain.Users;
import com.server.pitch.users.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService{
    @Autowired
    UsersMapper usersMapper;

    @Override
    public Users findById(String id){
        return new Users();
    }

    @Override
    public List<Users> list(){
        return usersMapper.list();
    }

    @Override
    public List<Users> hrList() {
        return usersMapper.hrList();
    }
}
