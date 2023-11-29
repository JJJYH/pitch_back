package com.server.pitch.hire.service;

import com.server.pitch.cv.domain.CV;
import com.server.pitch.hire.domain.FilteringRequest;
import com.server.pitch.hire.domain.JobPosting;
import com.server.pitch.hire.domain.Liked;
import com.server.pitch.hire.mapper.JobPostingMapper;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Log
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

    @Override
    public List<JobPosting> getAllJobPostingList() {
        return jobPostingMapper.getAllJobPostingList();
    }

    @Override
    public List<JobPosting> getAllJobPostingListById(String userId) {
        return jobPostingMapper.getAllJobPostingListById(userId);
    }

//    @Override
//    public List<JobPosting> findJobPostingAll(String orderType) {
//        return jobPostingMapper.selectJobPostingList(orderType);
//    }

    @Override
    public Map<String, Object> findJobPostingAll(FilteringRequest filteringRequest) {

        List<JobPosting> jobPostings = jobPostingMapper.selectJobPostingList(filteringRequest);

        Map<String, Object> result = new HashMap<>();
        result.put("orderType", filteringRequest.getOrderType());
        result.put("jobType", filteringRequest.getJobType());
        result.put("jobGroup", filteringRequest.getJobGroup());
        result.put("location", filteringRequest.getLocation());
        result.put("postingType", filteringRequest.getPostingType());
        result.put("searchKey", filteringRequest.getSearchKey());
        result.put("jobPostings", jobPostings);

        //log.info(result.toString());
        return result;
    }

    @Override
    public void createLiked(Liked liked) {
        jobPostingMapper.insertLiked(liked);
    }

    @Override
    public void deleteLiked(Liked liked) {
        jobPostingMapper.deleteLiked(liked);
    }

    @Override
    public List<Liked> findLikedByUserId(String user_id) {
        if (user_id == null || user_id.isEmpty()) {
            return null;
        }
        return jobPostingMapper.selectLikedByUserId(user_id);
    }

    @Override
    public List<JobPosting> findRecommendList(String userId) {
        return jobPostingMapper.selectRecommendList(userId);
    }

    @Override
    public List<CV> findApplyGender(int jobPostingNo) {
        return jobPostingMapper.selectApplyGender(jobPostingNo);
    }

    @Override
    public List<CV> findApplyAge(int jobPostingNo) {
        return jobPostingMapper.selectApplyAge(jobPostingNo);
    }

    @Override
    public List<CV> findApplyCert(int jobPostingNo) {
        return jobPostingMapper.selectApplyCert(jobPostingNo);
    }

}
