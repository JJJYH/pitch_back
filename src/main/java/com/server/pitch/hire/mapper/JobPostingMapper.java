package com.server.pitch.hire.mapper;

import com.server.pitch.hire.domain.JobPosting;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.domain.Liked;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface JobPostingMapper {
    public void insertJobPosting(JobPosting jobPosting);
    public void updateJobReqStatus(JobReq jobReq);
    public List<JobPosting> selectJobPostingList();
    public void insertLiked(Liked liked);
    public void deleteLiked(Liked liked);
    public List<Liked> selectLikedByUserId(String user_id);
}
