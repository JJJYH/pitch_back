package com.server.pitch.cv.service;

import com.server.pitch.cv.domain.CV;
import com.server.pitch.cv.domain.CVFile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CVService {

    public CV findAll();

    public String create(CV cv);

    public ResponseEntity<Object> crateFile(MultipartFile[] files, String endPath);

}
