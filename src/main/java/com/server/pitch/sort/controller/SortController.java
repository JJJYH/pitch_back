package com.server.pitch.sort.controller;

import com.server.pitch.sort.domain.ApplicantDetailResonse;
import com.server.pitch.sort.domain.ApplicantRequest;
import com.server.pitch.sort.domain.ApplicantResponse;
import com.server.pitch.sort.domain.PostingInfoResponse;
import com.server.pitch.sort.service.SortService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 선택된 공고의 지원자들을 선별/관리하는 로직을 처리하는 컨트롤러입니다.
 * @author soyoung
 */
@Log4j2
@RestController
@RequestMapping("/admin")
public class SortController {
    @Autowired
    SortService service;

    /**
     * 지원자 목록을 조회하는 api
     * @param job_posting_no 관리할 공고 번호
     * @return List<ApplicantResponse> 지원자 목록
     */
    @GetMapping("/{job_posting_no}/sort")
    public List<ApplicantResponse> sortAll(@PathVariable int job_posting_no, @RequestParam String type) {
        log.info("==================================applicant list controller=====================================");

        return service.findAll(new ApplicantRequest(job_posting_no, "", type));
    }

    /**
     * 지원자 목록을 조회하는 api
     * @param job_posting_no 관리할 공고 번호
     * @return List<ApplicantResponse> 지원자 목록
     */
    @GetMapping("/{job_posting_no}/info")
    public PostingInfoResponse postingInfoOne(@PathVariable int job_posting_no) {
        log.info("==================================posting info controller=====================================");

        return service.findInfoByPostingNo(job_posting_no);
    }


    /**
     * 지원자 합격/불합격 대기 상태를 처리하는 api
     * @param data 타입(합격대기/불합격대기)과 지원번호 목록
     */
    @PutMapping("/type")
    public void handleStatusType(@RequestBody List<Map<String, Object>> data) {
        log.info("==================================acceptance controller=====================================");
        service.statusTypeUpdate(data);
    }

    /**
     * 지원자 합격/불합격 처리를 하는 api
     * @param job_posting_no,data 타입(합격/불합격)과 발표 양식, 지원자 목록
     */
    @PutMapping("/{job_posting_no}/acceptance")
    public void handleAcceptance(@PathVariable int job_posting_no, @RequestBody Map<String, Object> data) {
        log.info("==================================acceptance controller=====================================");

        service.statusUpdate(data);
    }

    /**
     * 지원자 상세 정보를 조회하는 api
     * @param job_posting_no,apply_no  관리할 공고 번호
     * @return ApplicantDetailResonse 지원자 상세
     */
    @GetMapping("/{job_posting_no}/sort/{apply_no}/detail")
    public ApplicantDetailResonse cvOne(@PathVariable int job_posting_no, @PathVariable int apply_no) {
        log.info("==================================applicant detail controller=====================================");

        return service.findOne(apply_no);
    }

}

