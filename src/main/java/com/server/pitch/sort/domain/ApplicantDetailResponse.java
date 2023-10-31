package com.server.pitch.sort.domain;


import com.server.pitch.cv.domain.CV;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDetailResponse {
    private int apply_no; //apply 채용지원번호
    private String picture; //이력서 사진 base64 encoded string
    private String applicant_status; //apply 지원자상태
    private Date apply_date; //apply 지원일
    private String read_status; //apply 열람여부
    private String status_type; //apply 상태구분
    private String note; //apply 비고
    private CV cv; //cv 이력서
    private List<CandidateEval> evals; //candidate_eval 채용평가
}
