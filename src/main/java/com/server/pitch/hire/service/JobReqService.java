package com.server.pitch.hire.service;

import com.server.pitch.hire.domain.Interviewer;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.users.domain.Users;

import java.util.List;
import java.util.Map;

public interface JobReqService {
    public List<JobReq> findAll(String user_id);
    public List<JobReq> findAllByStatus(List<String> selectedStatus, String user_id);
    public JobReq findByNo(int job_req_no);
    public int createJobReq(JobReq jobReq);
    public void deleteJobReq(int job_req_no);
    public void deleteJobReqList(List<Integer> jobReqNo);
    public void modifyJobReq(JobReq jobReq);
    public List<JobReq> combinedSearchByThings(Map<String, Object> params);
    public List<Users> findUserWithDept();
    public void addInterviewersToJobReq(JobReq jobReq, List<String> interviewer_id);

}
