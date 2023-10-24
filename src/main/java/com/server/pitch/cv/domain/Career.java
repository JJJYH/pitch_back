package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Career")
public class Career {

    private int career_no;
    private int cv_no;
    private String user_id;

    private String company_name;
    private String cv_dept_name;
    private Date join_date;
    private Date quit_date;
    private String position;
    private String job;
    private int salary;
    private String note;

}
