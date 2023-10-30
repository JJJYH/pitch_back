package com.server.pitch.users.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Department")
public class Department {
    private int dept_no;
    private String dept_name;
    private int max_num;
}
