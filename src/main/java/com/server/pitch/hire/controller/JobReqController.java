package com.server.pitch.hire.controller;

import com.server.pitch.aop.GetUserAccessToken;
import com.server.pitch.hire.domain.Interviewer;
import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.domain.JobReqList;
import com.server.pitch.hire.domain.ReqFile;
import com.server.pitch.hire.service.JobReqService;
import com.server.pitch.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin/hire")
@AllArgsConstructor
@Log
@Log4j
public class JobReqController {
   private JobReqService jobReqService;

   @GetUserAccessToken
   @GetMapping("/reqlist")
   public List<JobReq> jobReqAll(Users loginUser){
       String user_id = loginUser.getUser_id();
       return jobReqService.findAll(user_id);
   }


   @GetUserAccessToken
   @PostMapping("/statuslist")
   public List<JobReq> jobReqAllByStatus(@RequestBody JobReqList jobReqList, Users loginUser){
       //log.info(jobReqStatus.toString());
       String user_id = loginUser.getUser_id();
       List<String> selectedStatus = jobReqList.getSelectedStatus();
       return jobReqService.findAllByStatus(selectedStatus, user_id);

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
       jobReqService.deleteReqFiles(jobReqLists.getJobReqNo());
       jobReqService.deleteJobReqList(jobReqLists.getJobReqNo());
   }

   @PutMapping("/update/{job_req_no}")
    public void jobReqReplace(@RequestBody JobReq jobReq, @PathVariable int job_req_no){
       log.info(jobReq.toString());
       jobReq.setJob_req_no(job_req_no);
       jobReqService.modifyJobReq(jobReq);
   }

   @PutMapping("/update/status")
   public void statusListUpdate(@RequestBody JobReqList jobReqList){
       jobReqService.modifyStatusList(jobReqList.getJobReqs());
   }

    @GetUserAccessToken
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


    @PostMapping("/upload")
    public void reqFileUpload(@RequestParam("files") List<MultipartFile> files, @RequestParam("jobReqNo") int jobReqNo) {
        try {
            jobReqService.createReqFiles(files, jobReqNo);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @GetMapping("/{jobReqNo}/files")
    public List<ReqFile> reqFileByJobReqNo(@PathVariable int jobReqNo){
        return jobReqService.findReqFiles(jobReqNo);
    }



    @GetMapping("/{reqFileNo}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable int reqFileNo, HttpServletResponse response) {
        ResponseEntity<Resource> result = jobReqService.downloadFile(reqFileNo);

        if (result.getStatusCode() == HttpStatus.OK) {
            Resource resource = result.getBody();
            String fileName = resource.getFilename();

            try {

                String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                        .replace("+", "%20");  // 공백 처리


                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return result;
    }



}
