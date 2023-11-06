package com.server.pitch.hire.controller;

import com.server.pitch.aop.GetUserAccessToken;
import com.server.pitch.hire.domain.Interviewer;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.domain.JobReqList;
import com.server.pitch.hire.service.JobReqService;
import com.server.pitch.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin/hire")
@AllArgsConstructor
@Log
public class JobReqController {
   private JobReqService jobReqService;

   @GetMapping("/reqlist")
   public List<JobReq> jobReqAll(){
       return jobReqService.findAll();
   }


   @PostMapping("/statuslist")
   public List<JobReq> jobReqAllByStatus(@RequestBody JobReqList jobReqLists){
       //log.info(jobReqStatus.toString());
       return jobReqService.findAllByStatus(jobReqLists.getSelectedStatus());

   }

   @GetMapping("/jobreq/{job_req_no}")
   public JobReq jobReqOne(@PathVariable("job_req_no") int job_req_no){
       return jobReqService.findByNo(job_req_no);
   }

   @GetUserAccessToken
   @PostMapping("/create")
    public int jobReqCreate(@RequestBody JobReq jobReq, Users loginUser){
       log.info(jobReq.toString());
       jobReq.setUsers(loginUser);
       return jobReqService.createJobReq(jobReq);
   }

   @DeleteMapping("/delete/{job_req_no}")
    public void jobReqDelete(@PathVariable("job_req_no") int job_req_no){
       jobReqService.deleteJobReq(job_req_no);
   }

   @DeleteMapping("/delete/checked")
   public void jobReqListDelete(@RequestBody JobReqList jobReqLists){
       jobReqService.deleteJobReqList(jobReqLists.getJobReqNo());
   }

   @PutMapping("/update/{job_req_no}")
    public void jobReqReplace(@RequestBody JobReq jobReq, @PathVariable int job_req_no){
       jobReq.setJob_req_no(job_req_no);
       jobReqService.modifyJobReq(jobReq);
   }


    @PostMapping("/search")
    public List<JobReq> combinedSearch(@RequestBody Map<String, Object> searchParams){
       log.info(searchParams.toString());
        return jobReqService.combinedSearchByThings(searchParams);
    }

    @GetMapping("/deptusers")
    public List<Users> deptUsersAll(){
       return jobReqService.findUserWithDept();
    }

    @PutMapping ("/addInterviewers/{job_req_no}")
    public void addInterviewersToJobReq(@RequestBody Interviewer interviewer) {
        JobReq jobReq = interviewer.getJobReq();
        List<String> interviewer_id = interviewer.getInterviewer_id();
        jobReqService.addInterviewersToJobReq(jobReq, interviewer_id);

    }


}
