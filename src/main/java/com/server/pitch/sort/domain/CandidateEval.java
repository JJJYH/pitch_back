package com.server.pitch.sort.domain;


import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("CandidateEval")
public class CandidateEval { //채용평가 테이블
    private int apply_no; //채용지원번호
    private int job_req_no; //채용요청번호
    private String user_id; //로그인아이디
    private int sub1_score; //항목1 점수
    private int sub2_score; //항목2 점수
    private String note; //비고
}
