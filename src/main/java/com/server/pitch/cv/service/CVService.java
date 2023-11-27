package com.server.pitch.cv.service;

import com.server.pitch.cv.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CVService {

    public CV findAll(CV cv);

    public List<CVFile> findCVFile(int cv_no, String user_id);

    public List<Apply> findApplyList(String user_id);

    public List<ChartData> findCountReq(int job_posting_no);
    public List<ChartData> findCountReqUser(int cv_no);

    public List<JobReq> findJobInfoList(int job_posting_no);

    public int create(CV cv);

    public String findPosition(int job_posting_no);

    public ResponseEntity<Object> createImageFile(CVFile imgCVFile);

    public ResponseEntity<Object> crateFile(MultipartFile[] files, String endPath,int cv_no,String user_id);

    public String modify(CV cv);

    public int findCVNO(CV cv);
    public int findMainCVNO(CV cv);
    public int findApplyCheck(int cv_no);
    public int createApply(CV cv, int apply_no);


}
