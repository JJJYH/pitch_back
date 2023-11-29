package com.server.pitch.sort.controller;

import com.server.pitch.sort.domain.*;
import com.server.pitch.sort.service.SortService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * 선택된 공고의 지원자들을 선별/관리하는 로직을 처리하는 컨트롤러입니다.
 *
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
     *
     * @param job_posting_no 관리할 공고 번호
     * @return List<ApplicantResponse> 지원자 목록
     */
    @GetMapping("/{job_posting_no}/sort")
    public List<ApplicantResponse> sortAll(@PathVariable int job_posting_no, @RequestParam String type) throws Exception {
        log.info("==================================applicant list controller=====================================");

        return service.findAll(new ApplicantRequest(job_posting_no, "", type));
    }

    /**
     * 공고 정보를 조회하는 api
     *
     * @param job_posting_no 관리할 공고 번호
     * @return PostingInfoResponse 공고 정보
     */
    @GetMapping("/{job_posting_no}/info")
    public PostingInfoResponse postingInfoOne(@PathVariable int job_posting_no) {
        log.info("==================================posting info controller=====================================");

        return service.findInfoByPostingNo(job_posting_no);
    }


    /**
     * 지원자 합격/불합격 대기 상태를 처리하는 api
     *
     * @param data 타입(합격대기/불합격대기)과 지원번호 목록
     */
    @PutMapping("/type")
    public void handleStatusType(@RequestBody List<Map<String, Object>> data) {
        log.info("==================================acceptance controller=====================================");

        service.statusTypeUpdate(data);
    }

    /**
     * 지원자 합격/불합격 처리를 하는 api
     *
     * @param job_posting_no,data 타입(합격/불합격)과 발표 양식, 지원자 목록
     */
    @PutMapping("/{job_posting_no}/acceptance")
    public void handleAcceptance(@PathVariable int job_posting_no, @RequestBody Map<String, Object> data) {
        log.info("==================================acceptance controller=====================================");

        service.statusUpdate(data);
    }

    /**
     * 지원자 상세 정보를 조회하는 api
     *
     * @param apply_no 지원자 번호
     * @return ApplicantDetailResonse 지원자 상세
     */
    @GetMapping("/{apply_no}/detail")
    public ApplicantDetailResponse cvOne(@PathVariable int apply_no) {
        log.info("==================================applicant detail controller=====================================");

        return service.findOne(apply_no);
    }

    /**
     * 지원자 면접 점수를 평가하는 api
     *
     * @param apply_no 평가할 지원자 번호
     */
    @PostMapping("/{apply_no}/evaluation")
    public void evaluate(@PathVariable int apply_no, @RequestBody CandidateEval eval) {
        log.info("==================================applicant evaluate controller=====================================");
        service.createEval(eval);
    }

    /**
     * 상세 점수 내역 조회하는 api
     *
     * @param apply_no,job_posting_no 지원번호, 공고번호
     * @return Score 점수 객체
     */
    @GetMapping("/{job_posting_no}/sort/{apply_no}")
    public Score scoreOne(@PathVariable int job_posting_no, @PathVariable int apply_no) {
        return service.selectScore(job_posting_no, apply_no);
    }

    /**
     * 지원자 평균 점수 조회하는 api
     *
     * @param job_posting_no 공고번호
     * @return ApplicantAvg
     */
    @GetMapping("/{job_posting_no}/average")
    public ApplicantAvgResponse applicantAvg(@PathVariable int job_posting_no) {
        return service.selectAvg(job_posting_no);
    }

    /**
     * 자기소개서 워드클라우드 api
     *
     * @return List<Map<String, Object>> 명사, 사용횟수
     */
    @GetMapping("/test-word")
    public List<Map<String, Object>> wordCloud() throws Exception {
        log.info("==================================word cloud controller=====================================");

        return service.createWordClouds(1);
    }

    /**
     * 선별된 지원자 리스트 api
     *
     * @param job_posting_no 공고번호
     * @return List<ApplicantDetailResponse>  선별된 지원자 목록
     */
    @PostMapping("/{job_posting_no}/filter")
    public List<ApplicantDetailResponse> filteredApplicantList(@PathVariable int job_posting_no, @RequestBody FilterRequest filter) {
        log.info("==================================filtered applicant list controller=====================================");
    
        return service.findAllFilteredApplicant(job_posting_no, filter);
    }

    /**
     * 입사지원서 엑셀로 저장 api
     * @param list 엑셀로 저장할 지원자들의 지원번호 목록
     */

    @PostMapping("/excel")
    public ResponseEntity<byte[]> cvToExcel(@RequestBody List<Integer> list) {
        log.info("==================================cv to excel controller=====================================");

        byte[] res = service.cvToExcel(list);

        HttpHeaders headers = new HttpHeaders();
        try {
            String encodedFilename = URLEncoder.encode("자기소개서.xlsx", StandardCharsets.UTF_8.toString());
            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(res.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(res);
    }
    /**
     * 압축파일로 저장 api
     * @param req 다운로드할 파일 종류와 지원자들의 지원번호 목록
     */
    @PostMapping(value = "/files", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> fileToZipDownload(@RequestBody FileDownloadRequest req) throws UnsupportedEncodingException {
        log.info("==================================file to zip download controller=====================================");

        String fileName = req.getTitle();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()));

        byte[] fileBytes = service.downloadFiles(req);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileBytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileBytes);
    }

    /**
     * 파일 저장 api
     * @param req 다운로드할 파일 종류와 지원자들의 지원번호 목록
     */
    @PostMapping(value = "/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> fileDownload(@RequestBody FileDownloadRequest req) throws UnsupportedEncodingException {
        log.info("==================================file download controller=====================================");

        byte[] res = service.downloadFile(req);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(req.getType().toString(), StandardCharsets.UTF_8.toString()));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(res.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(res);
    }

    @PutMapping("/{applyNo}/read")
    public void readStatus(@PathVariable int applyNo) {
        service.readStatusUpdate(applyNo);
    }

    @GetMapping("/checkScore/{applyNo}")
    public void testScore(@PathVariable int applyNo) {
        ApplicantDetailResponse applicant = service.findOne(applyNo);
        service.createScore(applyNo, applicant.getCv());
    }

    @GetMapping("/checkScore/{applyNo}/test")
    public void testScore2(@PathVariable int applyNo) {
        ApplicantDetailResponse applicant = service.findOne(applyNo);
        service.testScore(applyNo, applicant);
    }
}






