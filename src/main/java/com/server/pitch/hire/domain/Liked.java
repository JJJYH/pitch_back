package com.server.pitch.hire.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Liked")
public class Liked {
    private int job_posting_no;
    private String user_id;
}
