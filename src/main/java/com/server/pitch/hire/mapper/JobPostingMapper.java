package com.server.pitch.hire.mapper;

import com.server.pitch.hire.domain.JobPosting;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.domain.Liked;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface JobPostingMapper {
    public void insertJobPosting(JobPosting jobPosting);
    public void updateJobReqStatus(JobReq jobReq);
//    public List<JobPosting> selectJobPostingList();
    public List<JobPosting> selectJobPostingList(@Param("orderType") String orderType);

    public void insertLiked(Liked liked);
    public void deleteLiked(Liked liked);
    public List<Liked> selectLikedByUserId(String user_id);
}
