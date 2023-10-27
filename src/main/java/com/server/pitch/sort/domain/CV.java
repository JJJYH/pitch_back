package com.server.pitch.sort.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("CV")
public class CV { //이력서 테이블
    private int cv_no; //이력서번호
    private String user_id; //로그인아이디
    private String ps_statement; //자기소개서
    private String gender; //성별
    private String address; //주소
}
