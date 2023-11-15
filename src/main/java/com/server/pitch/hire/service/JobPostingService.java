package com.server.pitch.hire.service;

import com.server.pitch.hire.domain.FilteringRequest;
import com.server.pitch.hire.domain.JobPosting;
import com.server.pitch.hire.domain.Liked;

import java.util.List;
import java.util.Map;

public interface JobPostingService {
//    public void createJobPosting(JobPosting jobPosting);

    public void createJobPostingAndUpdateJobReqStatus(JobPosting jobPosting);
    public List<JobPosting> getAllJobPostingList();
//    public List<JobPosting> findJobPostingAll(String orderType);
    public Map<String, Object> findJobPostingAll(FilteringRequest filteringRequest);
    public void createLiked(Liked liked);
    public void deleteLiked(Liked liked);
    public List<Liked> findLikedByUserId(String user_id);
    List<JobPosting> findRecommendList(String userId);

}
