package com.server.pitch.sort.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("CVfle")
public class CVfile { //이력서 첨부파일
    private int cv_file_no; //첨부파일번호
    private int cv_no; //이력서번호
    private String user_id; //로그인아이디
    private String file_name; //파일명
    private String file_type; //파일형식
    private int file_size; //파일크기
    private Date upload_date; //업로드날짜
    private String path; //저장경로
}
