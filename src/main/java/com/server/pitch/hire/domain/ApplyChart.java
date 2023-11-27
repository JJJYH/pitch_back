package com.server.pitch.hire.domain;

import com.server.pitch.cv.domain.Certification;
import com.server.pitch.cv.domain.Language;
import com.server.pitch.cv.domain.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("ApplyChart")
public class ApplyChart {
    private int apply_no;
    private String user_id;
    private int job_posting_no;
    private String user_nm;
    private String user_birth;
    private String gender;
    private List<Certification> certifications;
    private List<Language> languages;
    private List<Skill> skills;
}
