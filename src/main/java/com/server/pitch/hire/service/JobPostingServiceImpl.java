package com.server.pitch.hire.service;

import com.server.pitch.hire.domain.JobPosting;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.mapper.JobPostingMapper;
import com.server.pitch.hire.mapper.JobReqMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobPostingServiceImpl implements JobPostingService{

    private JobPostingMapper jobPostingMapper;

//    @Override
//    public void createJobPosting(JobPosting jobPosting) {
//        jobPostingMapper.insertJobPosting(jobPosting);
//    }


    @Override
    public void createJobPostingAndUpdateJobReqStatus(JobPosting jobPosting) {
        jobPostingMapper.insertJobPosting(jobPosting);
        jobPostingMapper.updateJobReqStatus(jobPosting.getJobReq());

    }

}
