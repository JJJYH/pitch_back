package com.server.pitch.loginuser.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("LoginUser")
public class LoginUser {
    private String user_id;
    private String user_pw;
    private String user_nm;
    private String role;
    private String user_phone;
    private String user_birth;
}
