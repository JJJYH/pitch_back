package com.server.pitch.hire.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("ReqFile")
public class ReqFile {

    private int reqfile_no;
    private int job_req_no;
    private String file_name;
    private String file_type;
    private int file_size;
    private Date upload_date;
    private String path;

}
