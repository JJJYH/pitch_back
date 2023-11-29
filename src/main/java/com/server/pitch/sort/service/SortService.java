package com.server.pitch.sort.service;

import com.server.pitch.cv.domain.CV;
import com.server.pitch.sort.domain.*;

import java.util.List;
import java.util.Map;

public interface SortService {
    public List<ApplicantResponse> findAll(ApplicantRequest req) throws Exception;
    public ApplicantDetailResponse findOne(int applyNo);
    public List<ApplicantDetailResponse> findAllFilteredApplicant(int postingNo, FilterRequest filter);
    public String statusTypeUpdate (List<Map<String, Object>> data);
    public String statusUpdate(Map<String, Object> data);
    public PostingInfoResponse findInfoByPostingNo(int postingNo);
    public void createEval(CandidateEval eval);
    public List<String> doWordNouns(String text) throws Exception;
    public List<Map<String, Object>> doWordCount(List<String> pList) throws Exception;
    public List<Map<String, Object>> doWordAnalysis(String text) throws Exception;
    public Score selectScore(int job_posting_no, int apply_no);
    public ApplicantAvgResponse selectAvg(int job_posting_no);
    public byte[] cvToExcel(List<Integer> list);
    public byte[] downloadFiles(FileDownloadRequest req);
    public byte[] downloadFile(FileDownloadRequest req);
    public List<Map<String, Object>> createWordClouds(int apply_no);
    public void createScore(int apply_no, CV cv);
    public void testScore(int applyNo, ApplicantDetailResponse applicant);
    public void readStatusUpdate(int applyNo);
}
