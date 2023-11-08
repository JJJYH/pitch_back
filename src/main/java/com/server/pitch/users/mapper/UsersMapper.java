package com.server.pitch.users.mapper;

import com.server.pitch.users.domain.Department;
import com.server.pitch.users.domain.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UsersMapper {
    public List<Users> list();
    public List<Users> hrList();
    public Users selectUser(Users users);
    public void insertUser(Users users);
    public List<Department> deptList();
    public Department selectDept(String dept_name);
    public void updateHr(Users users);
}
