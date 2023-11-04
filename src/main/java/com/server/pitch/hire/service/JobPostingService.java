package com.server.pitch.hire.service;

import com.server.pitch.hire.domain.JobPosting;
import com.server.pitch.hire.domain.JobReq;

public interface JobPostingService {
//    public void createJobPosting(JobPosting jobPosting);

    public void createJobPostingAndUpdateJobReqStatus(JobPosting jobPosting);

}
