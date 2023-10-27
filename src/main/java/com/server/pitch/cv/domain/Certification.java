package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Certification")
public class Certification {

    private int cert_no;
    private int cv_no;
    private String user_id;

    private String cert_name;
    private String publisher;
    private Date acquisition_date;
}
