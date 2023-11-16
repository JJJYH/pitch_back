package com.server.pitch.hire.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilteringRequest {
    private String jobType;
    private String jobGroup;
    private String location;
    private String searchKey;
    private String postingType;
    private String orderType;
}
