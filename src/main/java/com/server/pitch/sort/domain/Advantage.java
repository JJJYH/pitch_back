package com.server.pitch.sort.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Advantage")
public class Advantage { //우대사항
    private int advantage_no; //우대사항번호
    private int cv_no; //이력서번호
    private String user_id; //로그인아이디
    private String advantage_type; //우대타입
    private String advantage_detail; //세부사항
    private String consent; //수집동의
}
