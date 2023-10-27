package com.server.pitch.sort.service;

import com.server.pitch.sort.domain.ApplicantRequest;
import com.server.pitch.sort.domain.ApplicantResponse;
import com.server.pitch.sort.domain.PostingInfoResponse;
import com.server.pitch.sort.mapper.SortMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class SortServiceImpl implements SortService{
    @Autowired
    SortMapper mapper;

    @Override
    public List<ApplicantResponse> findAll(ApplicantRequest req) {
        return mapper.selectSortList(req);
    }

    @Override
    public String statusTypeUpdate(List<Map<String, Object>> data) {
        data.forEach(row -> {
            try {
                mapper.updateStatus(row);
            } catch (Exception e) {
                throw new RuntimeException("쿼리 실행 중 오류 발생", e);
            }
        });
        return null;
    }

    @Override
    public String statusUpdate(Map<String, Object> data) {
        String type = (String)data.get("type"); //1, 2차 합격 = 평가 전, 최종 합격 = 최종합격, 불합격 = 서류,2차,최종 불합격
        String status = (String)data.get("status");
        String newStatus = "평가전";
        String contents = (String)data.get("contents");


        
        //합격처리할 리스트 먼저 뽑아옴 -> apply_no, email
        //type으로 pass/fail/all에 따라 다른 양식으로 메일이 발송되어야 함
        //서비스 메서드명도 바뀌어야 함
        //합격 발표 후 상태 변경 -> 다음 전형으로 & 대기 상태
        ApplicantRequest req = new ApplicantRequest(
                (Integer) data.get("job_posting_no"),
                (String) data.get("type"), //pass or fail
                (String) data.get("status")
        );

        List<ApplicantResponse> list = mapper.selectSortList(req); //조회할 때도 type 필터 추가해줘야함

        list.forEach(row -> {
//            if("pass".equals(status)) {
//                if("서류전형".equals()) {
//
//                }
//            } else if("fail".equals(status)) {
//
//            }

            Map<String, Object> map = new HashMap<>();

            map.put("applicant_status", status);
            map.put("status_type", type);
            map.put("apply_no", row.getApply_no());

            try {
                mapper.updateStatus(map);
            } catch (Exception e) {
                throw new RuntimeException("쿼리 실행 중 오류 발생", e);
            }
        });
        return null;
    }

    @Override
    public PostingInfoResponse findInfoByPostingNo(int job_posting_no) {
        return mapper.selectPostingInfo(job_posting_no);
    }
}
