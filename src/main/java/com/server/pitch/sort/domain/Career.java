package com.server.pitch.sort.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("Career")
public class Career { //경력사항 테이블 
    private int career_no; //경력사항번호
    private int cv_no; //이력서번호
    private String user_id; //로그인아이디
    private String company_name; //회사명
    private String cv_dept_name; //부서명
    private Date join_date; //입사년월
    private String position; //직급
    private String job; //직무
    private int salary; //연봉
    private String note; //비고
}
