package com.server.pitch.cv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("CVFile")
public class CVFile {

    private int cv_file_no;
    private int cv_no;
    private String user_id;

    private String file_name;
    private String file_type;
    private int file_size;
    private Date upload_date;
    private String path;
    private String type;
}
