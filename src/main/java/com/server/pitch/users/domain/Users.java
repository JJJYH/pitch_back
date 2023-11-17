package com.server.pitch.users.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Users")
public class Users {
    private String user_id;
    private String user_email;
    private String user_pw;
    private String user_nm;
    private String role;
    private String user_phone;
    private String user_birth;
    private String status;
    private Department department;
}
