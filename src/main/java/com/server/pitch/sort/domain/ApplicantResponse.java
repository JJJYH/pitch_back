package com.server.pitch.sort.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApplicantResponse {
    private int apply_no; //apply 채용지원번호
    private int cv_no; //apply 이력서번호
    private String picture; //이력서 사진 base64 encoded string
    private String user_nm; //users 이름
    private String user_birth; //users 생년월일
    private String user_email; //users 이메일
    private String applicant_status; //apply 지원자상태
    private Date apply_date; //apply 지원일
    private String read_status; //apply 열람여부
    private String status_type; //apply 상태구분
    private String gender; //cv 성별
    private String note; //apply 비고
    private String career; //경력여부  cv_no로 career 테이블에서 존재 여부
    private List<CandidateEval> evals; //candidate_eval 채용평가
}
