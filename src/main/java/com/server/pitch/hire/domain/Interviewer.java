package com.server.pitch.hire.domain;

import com.server.pitch.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Interviewer")
public class Interviewer {
    private JobReq jobReq;
    private Users users;
}
