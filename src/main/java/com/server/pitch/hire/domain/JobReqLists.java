package com.server.pitch.hire.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobReqLists {
    private List<String> selectedStatus;
    private List<Integer> jobReqNo;
}
