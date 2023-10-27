package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Activity")
public class Activity {

    private int activity_no;
    private int cv_no;
    private String user_id;

    private String activity_type;
    private String organization;
    private Date start_date;
    private Date end_date;
    private String activity_detail;
}
