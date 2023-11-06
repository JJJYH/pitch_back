package com.server.pitch.hire.controller;

import com.server.pitch.hire.domain.JobPosting;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.service.JobPostingService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hire")
@AllArgsConstructor
@Log
public class JobPostingController {
    private JobPostingService jobPostingService;

    @PostMapping("/create-post")
    public void createJobPostingAndUpdateStatus(@RequestBody JobPosting jobPosting){
        log.info(jobPosting.toString());
        jobPostingService.createJobPostingAndUpdateJobReqStatus(jobPosting);

    }

    @GetMapping("/getJobPostingList")
    public List<JobPosting> jobPostingAll(){
        return jobPostingService.findJobPostingAll();
    }
}
