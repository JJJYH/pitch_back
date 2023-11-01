package com.server.pitch.sort.service;

import com.server.pitch.sort.domain.*;

import java.util.List;
import java.util.Map;

public interface SortService {
    public List<ApplicantResponse> findAll(ApplicantRequest req);
    public ApplicantDetailResponse findOne(int applyNo);
    public String statusTypeUpdate (List<Map<String, Object>> data);
    public String statusUpdate(Map<String, Object> data);
    public PostingInfoResponse findInfoByPostingNo(int postingNo);
    public void createEval(CandidateEval eval);
}
