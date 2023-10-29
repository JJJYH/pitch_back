package com.server.pitch.sort.service;

import com.server.pitch.sort.domain.ApplicantDetailResonse;
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
    public ApplicantDetailResonse findOne(int applyNo) {
        return mapper.selectApplicant(applyNo);
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
        String type = (String) data.get("type");
        String statusType = (String) data.get("status_type");
        String status = "";
        String newStatus = "";
        String newStatusType = "평가전";

        if ("pass".equals(type)) {
            switch (statusType) {
                case "서류전형":
                    newStatus = "second";
                    status = "F";
                    break;
                case "면접전형":
                    newStatus = "final";
                    status = "S";
                    break;
                case "최종합격":
                    newStatus = "final";
                    newStatusType = "최종합격";
                    status = "FL";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid statusType: " + statusType);
            }
        } else if ("fail".equals(type)) {
            switch (statusType) {
                case "서류전형":
                    status = "F";
                    break;
                case "최종합격":
                    status = "FL";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid statusType: " + statusType);
            }
            newStatus = "finished";
            newStatusType = statusType.substring(0, 2) + " 불합격";
        } else {
            throw new IllegalArgumentException("Invalid type: " + type);
        }

        ApplicantRequest req = new ApplicantRequest(
                Integer.parseInt((String)data.get("job_posting_no")),
                type,
                status
        );

        List<ApplicantResponse> list = mapper.selectSortList(req);

        for(ApplicantResponse row : list) {
            Map<String, Object> map = new HashMap<>();

            map.put("applicant_status", newStatus);
            map.put("status_type", newStatusType);
            map.put("apply_no", row.getApply_no());

            try {
                mapper.updateStatus(map);
            } catch (Exception e) {
                throw new RuntimeException("쿼리 실행 중 오류 발생", e);
            }
        };

        return null;
    }


    @Override
    public PostingInfoResponse findInfoByPostingNo(int postingNo) {
        return mapper.selectPostingInfo(postingNo);
    }
}
