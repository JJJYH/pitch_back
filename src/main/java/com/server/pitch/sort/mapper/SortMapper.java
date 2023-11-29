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
    public void insertScore(Score score);
    public Score selectScore(int job_posting_no, int apply_no);
    public FilterRequest selectFilter(int postingNo);
    public ApplicantAvgResponse selectAvg(int job_posting_no);
    public List<ApplicantDetailResponse> selectFilteredApplicant(int postingNo);
    public void updateScore(int applyNo, int score);
    public void updateReadStatus(int applyNo);
}
