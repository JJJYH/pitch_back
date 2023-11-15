package com.server.pitch.hire.controller;

import com.server.pitch.aop.GetUserAccessToken;
import com.server.pitch.hire.domain.FilteringRequest;
import com.server.pitch.hire.domain.JobPosting;
import com.server.pitch.hire.domain.Liked;
import com.server.pitch.hire.service.JobPostingService;
import com.server.pitch.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/getAllJobPostingList")
    public List<JobPosting> jobPostingAll(){
        return jobPostingService.getAllJobPostingList();
    }

//    @GetMapping("/getJobPostingList")
//    public List<JobPosting> jobPostingAll(@RequestParam(name = "orderType", required = false) String orderType){
//        return jobPostingService.findJobPostingAll(orderType);
//    }

    @PostMapping("/getJobPostingList")
    public Map<String, Object> jobPostingAll(@RequestBody FilteringRequest filteringRequest) {
        Map<String, Object> result = jobPostingService.findJobPostingAll(filteringRequest);
        //log.info(result.toString());

        return result;
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

//    @GetUserAccessToken
//    @GetMapping("/liked")
//    public List<Liked> getLikedByUserId(Users loginUser) {
//        String user_id = loginUser.getUser_id();
//        return jobPostingService.findLikedByUserId(user_id);
//    }

    @GetUserAccessToken
    @GetMapping("/liked")
    public List<Liked> getLikedByUserId(Users loginUser) {
        String user_id = (loginUser != null) ? loginUser.getUser_id() : null;
        // user_id가 null이면 빈 리스트 반환
        return (user_id != null) ? jobPostingService.findLikedByUserId(user_id) : Collections.emptyList();
    }

    @GetUserAccessToken
    @GetMapping("/getRecommendList")
    public List<JobPosting> recommendPostingAll(Users loginUser){
        String userId = loginUser.getUser_id();
        //log.info(userId);
        return jobPostingService.findRecommendList(userId);
    }

}
