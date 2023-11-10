package com.server.pitch.hire.mapper;

import com.server.pitch.hire.domain.Interviewer;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.users.domain.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface JobReqMapper {
    public List<JobReq> selectJobReqList(String user_id);
    public  List<JobReq> selectJobReqStatusList(List<String> selectedStatus, String user_id);
    public JobReq selectJobReq(int job_req_no);
    public void insertJobReq(JobReq jobReq);
    public void deleteJobReq(int job_req_no);
    public void deleteJobReqList(List<Integer> jobReqNo);
    public void updateJobReq(JobReq jobReq);
    public List<JobReq> combinedSearchByThings(Map<String, Object> params);
    public List<Users> selectUserWithDept();
    public void insertInterviewer(@Param("job_req_no") int job_req_no, @Param("interviewer_id") String interviewer_id);

}
