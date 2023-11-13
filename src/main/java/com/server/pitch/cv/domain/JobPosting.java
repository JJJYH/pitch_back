package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("JobPostingCV")
public class JobPosting {
    private int job_posting_no;
    private int job_req_no;
    private String posting_status;
    private boolean isLiked;
}
