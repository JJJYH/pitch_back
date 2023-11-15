package com.server.pitch.sort.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("ApplicantAvgResponse")
public class ApplicantAvgResponse {

    private int job_posting_no;
    private int avgScore;
    private int avgExp;
    private double avgCert;
    private double avgSkill;
    private double avgLang;
    private double avgAd;
    private double avgAc;
}
