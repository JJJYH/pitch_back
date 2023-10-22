package com.server.pitch.cv.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@RestController
@RequestMapping("/user/cv")
public class CVController {

//    String path = "C:\\dev\\test\\새폴더"; //폴더 경로
//    File Folder = new File(path);
//
//    // 해당 디렉토리가 없다면 디렉토리를 생성.
//	if (!Folder.exists()) {
//        try{
//            Folder.mkdir(); //폴더 생성합니다. ("새폴더"만 생성)
//            System.out.println("폴더가 생성완료.");
//        }
//        catch(Exception e){
//            e.getStackTrace();
//        }
//    }else {
//        System.out.println("폴더가 이미 존재합니다..");
//    }
@PostMapping("imageUpload")
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
        String filename = file.getOriginalFilename();
        String savePath = uploadPath + File.separator + filename;
        file.transferTo(new File(savePath));
        return ResponseEntity.ok("이미지 업로드 성공!");
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("이미지 파일을 저장하는 중 오류가 발생했습니다.");
    }
}
}
