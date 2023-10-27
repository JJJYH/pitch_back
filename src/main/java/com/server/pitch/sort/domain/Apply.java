package com.server.pitch.sort.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;


@Data
@Alias("Apply")
public class Apply { //채용지원 테이블
    private int apply_no; //채용지원번호
    private int cv_no; //이력서번호
    private String user_id; //로그인아이디
    private int job_posting_no; //채용공고번호
    private Date apply_date; //지원일
    private String applicant_status; //지원자상태
    private String status_type; //상태구분
    private String note; //비고
    private String read_status; //열람여부
}
