package com.server.pitch.hire.controller;

import com.server.pitch.aop.GetUserAccessToken;
import com.server.pitch.hire.domain.JobPosting;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.domain.Liked;
import com.server.pitch.hire.service.JobPostingService;
import com.server.pitch.users.domain.Users;
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
    public void jobPostingAndUpdateStatusCreate(@RequestBody JobPosting jobPosting){
        log.info(jobPosting.toString());
        jobPostingService.createJobPostingAndUpdateJobReqStatus(jobPosting);

    }

    @GetMapping("/getJobPostingList")
    public List<JobPosting> jobPostingAll(){
        return jobPostingService.findJobPostingAll();
    }

    @GetUserAccessToken
    @PostMapping("/liked")
    public void likedCreate(@RequestBody Liked liked, Users loginUser){
        liked.setUser_id(loginUser.getUser_id());
        jobPostingService.createLiked(liked);
    }

    @GetUserAccessToken
    @DeleteMapping("/liked")
    public void likedDelete(@RequestBody Liked liked, Users loginUser){
        liked.setUser_id(loginUser.getUser_id());
        jobPostingService.deleteLiked(liked);
    }

    @GetUserAccessToken
    @GetMapping("/liked")
    public List<Liked> getLikedByUserId(Users loginUser) {
        String user_id = loginUser.getUser_id();
        return jobPostingService.findLikedByUserId(user_id);
    }


}
