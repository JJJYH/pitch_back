package com.server.pitch.hire.service;

import com.server.pitch.hire.domain.JobPosting;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.domain.Liked;

import java.util.List;

public interface JobPostingService {
//    public void createJobPosting(JobPosting jobPosting);

    public void createJobPostingAndUpdateJobReqStatus(JobPosting jobPosting);
//    public List<JobPosting> findJobPostingAll();
    public List<JobPosting> findJobPostingAll(String orderType);
    public void createLiked(Liked liked);
    public void deleteLiked(Liked liked);
    public List<Liked> findLikedByUserId(String user_id);

}
