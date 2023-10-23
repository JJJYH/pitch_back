package com.server.pitch.hire.service;

import com.server.pitch.hire.domain.JobReq;

import java.util.List;

public interface JobReqService {
    public List<JobReq> findAll();
    public List<JobReq> findAllByStatus(List<String> selectedStatus);
    public JobReq findByNo(int job_req_no);
    public void createJobReq(JobReq jobReq);
    public void deleteJobReq(int job_req_no);
    public void modifyJobReq(JobReq jobReq);

}
