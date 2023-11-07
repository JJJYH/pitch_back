package com.server.pitch.hire.domain;

import com.server.pitch.users.domain.Department;
import com.server.pitch.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("JobPosting")
public class JobPosting {
    private int job_posting_no;
    private String posting_status;

    private JobReq jobReq;
    private Users users;
    private Department department;

    private boolean isLiked;
}
