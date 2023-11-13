package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Apply")
public class Apply {

    private int apply_no; // 채용지원번호
    private int cv_no; //  이력서 번호
    private String user_id; // 지원자 ID
    private int job_posting_no; // 공고 번호
    private Date apply_date; //지원일
    private String applicant_status; //평가 단계
    private String note; //비고
    private String read_status; // 열람 여부
    private String status_type; // 상태 구분
}
