package com.server.pitch.sort.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Score")
public class Score {

    private int score_no;
    private int apply_no;

    private int applicant_count;
    private int score;
    private int advantage_score;
    private int career_score;
    private int certification_score;
    private int language_score;
    private int education_score;

    private int total_rank;
    private int advantage_rank;
    private int career_rank;
    private int certification_rank;
    private int language_rank;
    private int education_rank;

    private int advantage_avg;
    private int career_avg;
    private int certification_avg;
    private int language_avg;
    private int education_avg;
}
