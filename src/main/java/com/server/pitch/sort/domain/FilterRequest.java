package com.server.pitch.sort.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
    private int advantageScore; //우대사항
    private int careerScore; //경력
    private int certificationScore; //자격증
    private int languageScore; //어학점수
    private int educationScore; //학력
    private String job_type; //신입 or 경력
    private String job_year; //경력기간
    private String job_group; //직군
    private String education; //학력구분
}
