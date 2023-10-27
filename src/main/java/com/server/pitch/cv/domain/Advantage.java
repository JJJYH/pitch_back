package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Advantage")
public class Advantage {

    private int advantage_no;
    private int cv_no;
    private String user_id;

    private String advantage_type;
    private String advantage_detail;
    private String consent;

}
