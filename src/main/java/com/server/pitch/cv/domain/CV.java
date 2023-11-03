package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("CV")
public class CV {

    //기본 테이블 정보
    private int cv_no;
    private String user_id;

    private String ps_statement;
    private String gender;
    private String address;
    //모집 공고에 대한 직무
    private String position;
    //추가 정보
    private String user_nm;
    private String user_phone;
    private String user_email;
    private String user_birth;

    //연관관계 매핑
    private List<Activity> activities;
    private List<Advantage> advantages;
    private List<Career> careers;
    private List<Certification> certifications;
    private List<CVFile> cvFiles;
    private List<Education> educations;
    private List<Language> languages;
    private List<Skill> skills;
}
