package com.server.pitch.users.mapper;

import com.server.pitch.users.domain.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UsersMapper {
    public List<Users> list();
    public Users selectUser(Users users);
    public void insertUser(Users users);
}
