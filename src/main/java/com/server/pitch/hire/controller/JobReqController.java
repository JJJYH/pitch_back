package com.server.pitch.hire.controller;

import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.domain.JobReqStatus;
import com.server.pitch.hire.service.JobReqService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
   public List<JobReq> jobReqAllByStatus(@RequestBody JobReqStatus jobReqStatus){
       //log.info(jobReqStatus.toString());
       return jobReqService.findAllByStatus(jobReqStatus.getSelectedStatus());

   }

   @GetMapping("/jobreq/{job_req_no}")
   public JobReq jobReqOne(@PathVariable("job_req_no") int job_req_no){
       return jobReqService.findByNo(job_req_no);
   }

   @PostMapping("/create")
    public void jobReqCreate(@RequestBody JobReq jobReq){
       jobReqService.createJobReq(jobReq);
   }

   @DeleteMapping("/delete/{job_req_no}")
    public void jobReqDelete(@PathVariable("job_req_no") int job_req_no){
       jobReqService.deleteJobReq(job_req_no);
   }

   @PutMapping("/update/{job_req_no}")
    public void jobReqReplace(@RequestBody JobReq jobReq, @PathVariable int job_req_no){
       jobReq.setJob_req_no(job_req_no);
       jobReqService.modifyJobReq(jobReq);
   }
}
