package com.server.pitch.sort.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("Education")
public class Education { //학력 테이블
    private int edu_no; //학력번호
    private int cv_no; //이력서번호
    private String user_id; //로그인아이디
    private String edu_type; //학력구분
    private Date enter_date; //입학년월
    private Date graduate_date; //졸업년월
    private String graduate_type; //졸업상태
    private String major; //전공명
    private double score; //학점
    private double total_score; //총점
}
