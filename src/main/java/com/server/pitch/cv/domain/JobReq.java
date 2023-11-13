package com.server.pitch.cv.domain;

import com.server.pitch.users.domain.Department;
import com.server.pitch.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("JobReqCV")
public class JobReq {
    private int job_req_no;
    private String user_id;
    private String req_title;
    private Date job_req_date;
    private String job_group;
    private String job_role;
    private String location;
    private int hire_num;
    private String education;
    private String job_type;
    private String job_year;
    private String posting_type;
    private String posting_period;
    private Date posting_start;
    private Date posting_end;
    private String qualification;
    private String preferred;
    private String job_duties;
    private String req_status;

    private JobPosting job_posting;

}
