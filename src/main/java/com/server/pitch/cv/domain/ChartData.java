package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("ChartData")
public class ChartData {
    private double career_count;
    private double activity_count;
    private double skill_count;
    private double advantage_count;
    private double cert_count;
    private double avg_score;
    private double lang_count;

}
