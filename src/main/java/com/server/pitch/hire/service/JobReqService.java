package com.server.pitch.hire.service;

import com.server.pitch.hire.domain.Interviewer;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.domain.ReqFile;
import com.server.pitch.users.domain.Users;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public void modifyStatusList(List<JobReq> jobReqs);
    public List<JobReq> combinedSearchByThings(Map<String, Object> params);
    public List<Users> findUserWithDept();
    public void addInterviewersToJobReq(JobReq jobReq, List<String> interviewer_id);
    public void createReqFiles(List<MultipartFile> files, int jobReqNo) throws IOException;
    public List<ReqFile> findReqFiles(int jobReqNo);
    public ReqFile selectReqFilesByFileNo(int reqfile_no);
    ResponseEntity<Resource> downloadFile(int reqFileNo);
    public void deleteReqFiles(List<Integer> jobReqNo);

}
