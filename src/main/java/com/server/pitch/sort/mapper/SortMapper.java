package com.server.pitch.sort.mapper;

import com.server.pitch.sort.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SortMapper {
    public List<ApplicantResponse> selectSortList(ApplicantRequest req);
    public ApplicantDetailResponse selectApplicant(int apply_no);
    public int updateStatusType(Map<String, Object> data);
    public PostingInfoResponse selectPostingInfo(int job_posting_no);
    public int updateStatus(Map<String, Object> data);
    public int insertEval(CandidateEval eval);
    public List<ApplicantDetailResponse> selectFilteredApplicant(int postingNo);
}
