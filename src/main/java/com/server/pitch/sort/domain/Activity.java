package com.server.pitch.sort.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("Activity")
public class Activity { //대외활동 테이블
    private int activity_no; //대외활동번호
    private int cv_no; //이력서번호
    private String user_id; //로그인아이디
    private String activity_type; //활동구분
    private String organization; //기관명
    private Date start_date; //시작년월
    private Date end_date; //종료년월
    private String activity_detail; //활동내용
}
