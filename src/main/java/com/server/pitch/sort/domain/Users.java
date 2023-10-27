package com.server.pitch.sort.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("User")
public class Users { //로그인사용자 테이블
    private String user_id; //로그인아이디
    private int dept_no; //부서번호
    private String user_email; //이메일
    private String user_pw; //비밀번호
    private String user_nm; //이름
    private String role; //역할
    private String user_phone; //전화번호
    private String user_birth; //생년월일
}
