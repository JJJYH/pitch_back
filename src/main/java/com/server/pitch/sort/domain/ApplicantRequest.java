package com.server.pitch.sort.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@NoArgsConstructor
@Alias("ApplicantRequest")
@AllArgsConstructor
public class ApplicantRequest {
    private int job_posting_no;
    private String type;
    private String status;
}
