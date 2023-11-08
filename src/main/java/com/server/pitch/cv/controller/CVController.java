package com.server.pitch.cv.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.pitch.aop.GetUserAccessToken;
import com.server.pitch.cv.domain.CV;
import com.server.pitch.cv.domain.CVFile;
import com.server.pitch.cv.service.CVService;
import com.server.pitch.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/main/cv")
public class
CVController {

    @Autowired
    public CVService cvService;
    @GetUserAccessToken
    @PostMapping("/imageUpload")
    public ResponseEntity<String> handleFileUpload(Users loginUSer,@RequestParam("image") MultipartFile file,@RequestParam("cv_no")int cv_no) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("업로드한 파일이 비어 있습니다.");
        }

        // 원하는 경로
        String uploadPath = "C:\\pitch_resorces\\images";
        CVFile imgCVFile = new CVFile();
        imgCVFile.setCv_no(cv_no);
        imgCVFile.setUser_id(loginUSer.getUser_id());
        imgCVFile.setFile_size((int)file.getSize());
        imgCVFile.setUpload_date(new Date(System.currentTimeMillis()));
        imgCVFile.setType("images");

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
            String newFileName = uuid + fileExtension+"_" + originalFilename;
            imgCVFile.setFile_name(newFileName);
            imgCVFile.setFile_type(fileExtension);
            String savePath = uploadPath + File.separator + newFileName;
            imgCVFile.setPath(savePath);
            cvService.createImageFile(imgCVFile);
            file.transferTo(new File(savePath));
            return ResponseEntity.ok(newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("이미지 파일을 저장하는 중 오류가 발생했습니다.");
        }
    }

    @GetUserAccessToken
    @GetMapping("/list")
    public ResponseEntity<Object> CVAll( Users loginUSer,@RequestParam("cv_no")int cv_no){
        log.info("CVAll Param is : "+cv_no);
        log.info("Login Info is : " + loginUSer.toString());
        CV cv = new CV();
        cv.setCv_no(cv_no);
        cv.setUser_id(loginUSer.getUser_id());

//        log.info(cvService.findAll(Integer.parseInt(request.getParameter("cv_no"))).toString());
        return ResponseEntity.ok(cvService.findAll(cv));
    }

    @GetMapping("/find-position")
    public ResponseEntity<Object> getPosition(@RequestParam("job_posting_no") int job_posting_no){
        String position = cvService.findPosition(job_posting_no);
        return ResponseEntity.ok(position);
    }

    @GetUserAccessToken
    @GetMapping("/init-cv")
    public ResponseEntity<Object> InitCV(Users loginUSer){
        CV cv = new CV();
        cv.setUser_id(loginUSer.getUser_id());
        cv.setUser_nm(loginUSer.getUser_nm());
        cv.setUser_email(loginUSer.getUser_email());
        cv.setUser_phone(loginUSer.getUser_phone());
        cv.setUser_birth(loginUSer.getUser_birth());


        log.info("Init Profile Setting : "+ cv);
        return ResponseEntity.ok(cv);
    }
    @GetUserAccessToken
    @GetMapping("/get-cv-no")
    public ResponseEntity<Object> getCVNO(Users loginUSer,@RequestParam("job_posting_no") int job_posting_no){

        CV cv = new CV();
        cv.setUser_id(loginUSer.getUser_id());
        cv.setJob_posting_no(job_posting_no);
        int result = cvService.findCVNO(cv);

        return ResponseEntity.ok(result);
    }


    @PostMapping
    public ResponseEntity<Integer> CVCrate(@RequestBody Map<String,Object> requestBody){
        log.info("Req Data : "+requestBody.toString());
        ObjectMapper mapper = new ObjectMapper();
        CV cv = mapper.convertValue(requestBody.get("cv"),CV.class);
        log.info("CV Data: " + cv);
        int cv_no = cvService.create(cv);
        return ResponseEntity.status(HttpStatus.OK).body(cv_no);
    }

    @PutMapping
    public ResponseEntity<Object> CVReplace(@RequestBody Map<String,Object> requestBody){
        log.info("Req Data : "+requestBody.toString());
        ObjectMapper mapper = new ObjectMapper();
        CV cv = mapper.convertValue(requestBody.get("cv"),CV.class);
        log.info("CV Data: " + cv);
        cvService.modify(cv);
        return ResponseEntity.ok("Connect");
    }

    @GetUserAccessToken
    @PostMapping("/cvFileUpload")
    public ResponseEntity<Object> CVFileUpload(Users loginUSer,@RequestParam("cvfile") MultipartFile[] files, @RequestParam("endPath") String endPath,@RequestParam("cv_no")int cv_no) {
        return cvService.crateFile(files,endPath,cv_no, loginUSer.getUser_id());
    }
}
