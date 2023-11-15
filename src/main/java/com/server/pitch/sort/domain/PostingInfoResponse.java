package com.server.pitch.sort.domain;

import lombok.Data;

import java.util.Date;

@Data
public class PostingInfoResponse {
    private int job_req_no; //jobposting 채용요청번호
    private int job_posting_no; //jobposting 채용공고번호
    private String posting_status; //jobposting 공고상태
    private String job_year; //jobreq 경력기간
    private String education; //jobreq 학력구분
    private String req_title; //jobreq 제목
    private String job_group;  //jobreq 직군
    private String job_role;  //jobreq 직무
    private int hire_num; //jobreq 채용인원
    private  int hired_num; //apply에서 status_type이 '최종합격'인 사람의 수
    private String job_type; // jobreq 채용형태
    private Date posting_start; //jobreq 공고시작
    private Date posting_end; //jobreq 공고종료
    private String posting_type; //jobreq 공고타입
    private int likedCnt; // job_posting_no로 liked 테이블에서 count
    private int total_applicants; //job_posting_no로 apply 테이블에서 count
}
