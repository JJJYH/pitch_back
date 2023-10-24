package com.server.pitch.cv.controller;

import com.server.pitch.cv.service.CVService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/main/cv")
public class CVController {

    @Autowired
    public CVService cvService;

    @PostMapping("/imageUpload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("업로드한 파일이 비어 있습니다.");
        }

        // 원하는 경로
        String uploadPath = "C:\\DouZone\\workspace\\react_work\\pitch_frontend\\public\\images";

        // 해당 경로에 디렉토리가 없다면 생성
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            try {
                if (folder.mkdirs()) {
                    System.out.println("폴더가 생성 완료.");
                } else {
                    return ResponseEntity.badRequest().body("폴더를 생성하지 못했습니다.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("서버 오류로 인해 폴더 생성에 실패했습니다.");
            }
        } else {
            System.out.println("폴더가 이미 존재합니다.");
        }
        // 이미지 파일을 저장
        try {

            // 원래 파일 이름과 확장자 추출
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // UUID 생성
            String uuid = UUID.randomUUID().toString();

            // UUID를 파일 이름에 붙여 새로운 파일 이름 생성
            String newFileName = uuid + fileExtension;
            String savePath = uploadPath + File.separator + newFileName;
            file.transferTo(new File(savePath));
            return ResponseEntity.ok(newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("이미지 파일을 저장하는 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> CVAll(){
        log.info(cvService.findAll().toString());
        return ResponseEntity.ok(cvService.findAll());
    }
}
