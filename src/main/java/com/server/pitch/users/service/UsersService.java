package com.server.pitch.users.service;

import com.server.pitch.users.domain.Users;

import java.util.List;

public interface UsersService {
    public Users findById(String id);
    public List<Users> list();
    public List<Users> hrList();
    public List<Users> noneAppHrList();
}
