package com.server.pitch.hire.service;

import com.server.pitch.hire.domain.JobReq;

import java.util.List;
import java.util.Map;

public interface JobReqService {
    public List<JobReq> findAll();
    public List<JobReq> findAllByStatus(List<String> selectedStatus);
    public JobReq findByNo(int job_req_no);
    public int createJobReq(JobReq jobReq);
    public void deleteJobReq(int job_req_no);
    public void deleteJobReqList(List<Integer> jobReqNo);
    public void modifyJobReq(JobReq jobReq);
    public List<JobReq> combinedSearchByThings(Map<String, Object> params);

}
