package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Language")
public class Language {

    private int language_no;
    private int cv_no;
    private String user_id;

    private String exam_type;
    private String language_name;
    private double language_score;
    private String language_level;
}
