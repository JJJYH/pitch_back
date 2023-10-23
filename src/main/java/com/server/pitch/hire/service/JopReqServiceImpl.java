package com.server.pitch.hire.service;

import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.mapper.JobReqMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class JopReqServiceImpl implements JobReqService {
    private JobReqMapper jobReqMapper;


    @Override
    public List<JobReq> findAll() {
        return jobReqMapper.selectJobReqList();
    }

    @Override
    public List<JobReq> findAllByStatus(List<String> selectedStatus) {
        return jobReqMapper.selectJobReqStatusList(selectedStatus);
    }

    @Override
    public JobReq findByNo(int job_req_no) {
        return jobReqMapper.selectJobReq(job_req_no);
    }

    @Override
    public void createJobReq(JobReq jobReq) {
        jobReqMapper.insertJobReq(jobReq);
    }

    @Override
    public void deleteJobReq(int job_req_no) {
        jobReqMapper.deleteJobReq(job_req_no);
    }

    @Override
    public void modifyJobReq(JobReq jobReq) {
        jobReqMapper.updateJobReq(jobReq);
    }
}
