package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Education")
public class Education {

    private int edu_no;
    private int cv_no;
    private String user_id;

    private String edu_type;
    private Date enter_date;
    private Date graduate_date;
    private String graduate_type;
    private String major;
    private double score;
    private double total_score;
}
