package com.server.pitch.sort.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("Certification")
public class Certification { //자격증 테이블
    private int cert_no; //자격증번호
    private int cv_no; //이력서번호
    private String user_id; //로그인아이디
    private String cert_name; //자격증명
    private String publisher; //발행처
    private Date acquisition_date; //취득년월
}
