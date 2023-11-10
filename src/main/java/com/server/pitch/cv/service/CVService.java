package com.server.pitch.cv.service;

import com.server.pitch.cv.domain.CV;
import com.server.pitch.cv.domain.CVFile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CVService {

    public CV findAll(CV cv);

    public List<CVFile> findCVFile(int cv_no, String user_id);

    public int create(CV cv);

    public String findPosition(int job_posting_no);

    public ResponseEntity<Object> createImageFile(CVFile imgCVFile);

    public ResponseEntity<Object> crateFile(MultipartFile[] files, String endPath,int cv_no,String user_id);

    public String modify(CV cv);

    public int findCVNO(CV cv);

    public int createApply(CV cv, int apply_no);


}
