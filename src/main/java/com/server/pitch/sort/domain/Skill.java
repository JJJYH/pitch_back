package com.server.pitch.sort.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Skill")
public class Skill { //보유기술 테이블
    private int skill_no; //보유기술번호
    private int cv_no; //이력서번호
    private String user_id; //로그인 아이디
    private String skill_name; //보유기술명
    private String skill_domain; //기술분류
}
