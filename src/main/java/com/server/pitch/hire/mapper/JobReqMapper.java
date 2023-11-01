package com.server.pitch.hire.mapper;

import com.server.pitch.hire.domain.JobReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface JobReqMapper {
    public List<JobReq> selectJobReqList();
    public  List<JobReq> selectJobReqStatusList(List<String> selectedStatus);
    public JobReq selectJobReq(int job_req_no);
    public void insertJobReq(JobReq jobReq);
    public void deleteJobReq(int job_req_no);
    public void deleteJobReqList(List<Integer> jobReqNo);
    public void updateJobReq(JobReq jobReq);
    public List<JobReq> combinedSearchByThings(Map<String, Object> params);

}
