package com.server.pitch.sort.service;

import com.server.pitch.sort.domain.ApplicantDetailResonse;
import com.server.pitch.sort.domain.ApplicantRequest;
import com.server.pitch.sort.domain.ApplicantResponse;
import com.server.pitch.sort.domain.PostingInfoResponse;

import java.util.List;
import java.util.Map;

public interface SortService {
    public List<ApplicantResponse> findAll(ApplicantRequest req);
    public ApplicantDetailResonse findOne(int applyNo);
    public String statusTypeUpdate (List<Map<String, Object>> data);
    public String statusUpdate(Map<String, Object> data);
    public PostingInfoResponse findInfoByPostingNo(int postingNo);
}
