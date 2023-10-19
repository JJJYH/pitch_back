package com.server.pitch.loginuser.mapper;

import com.server.pitch.loginuser.domain.LoginUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginUserMapper {
    public List<LoginUser> list();
}
