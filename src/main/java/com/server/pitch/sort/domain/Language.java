package com.server.pitch.sort.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Language")
public class Language { //어학점수 테이블
    private int language_no; //어학성적번호
    private int cv_no; //이력서번호
    private String user_id; //로그인아이디
    private String exam_type; //시험구분
    private String language_name; //외국어명
    private double language_score; //어학점수
}
