package com.server.pitch.hire.service;

import com.server.pitch.hire.domain.JobPosting;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.domain.Liked;
import com.server.pitch.hire.mapper.JobPostingMapper;
import com.server.pitch.hire.mapper.JobReqMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

//    @Override
//    public List<JobPosting> findJobPostingAll() {
//        return jobPostingMapper.selectJobPostingList();
//    }

    @Override
    public List<JobPosting> findJobPostingAll(String orderType) {
        return jobPostingMapper.selectJobPostingList(orderType);
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
        return jobPostingMapper.selectLikedByUserId(user_id);
    }

}
