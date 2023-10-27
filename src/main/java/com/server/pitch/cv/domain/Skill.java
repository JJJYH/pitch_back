package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Skill")
public class Skill {

    private int skill_no;
    private int cv_no;
    private String user_id;

    private String skill_name;
    private String skill_domain;
}
