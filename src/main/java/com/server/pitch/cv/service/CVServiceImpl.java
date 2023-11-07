package com.server.pitch.cv.service;

import com.server.pitch.cv.domain.Activity;
import com.server.pitch.cv.domain.CV;
import com.server.pitch.cv.domain.CVFile;
import com.server.pitch.cv.mapper.CVMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CVServiceImpl implements CVService {

    @Autowired
    CVMapper cvMapper;

    @Override
    public CV findAll(CV cv) {
        return cvMapper.selectCVList(cv);
    }

    @Override
    public String findPosition(int job_posting_no) {
        return cvMapper.selectPosition(job_posting_no);
    }

    @Override
    public ResponseEntity<Object> createImageFile(CVFile imgCVFile) {
        log.info("ImgCVFile Is : "+imgCVFile);
        cvMapper.insertCVFile(imgCVFile);
        return ResponseEntity.ok("이미지 파일이 성공적으로 업로드되었습니다.");
    }

    @Override
    public int findCVNO(CV cv) {
        return cvMapper.selectCVNO(cv);
    }

    @Override
    @Transactional
    public String modify(CV cv) {
        //CV update
        cvMapper.updateCV(cv);
        log.info("CV DATA IS UPDATE : " + cv.toString());
        cv.getActivities()
                .stream()
                .forEach(activity ->
                {
                    activity.setCv_no(cv.getCv_no());
                    activity.setUser_id(cv.getUser_id());
                    log.info("CV DATA IS UPDATE(Activity) : " + activity.toString());
                    cvMapper.updateActivity(activity);
                    if(activity.getActivity_no() ==0){
                        log.info("기존에 작성된 대외활동이 없으므로 post 요청 보냅니다.");
                        cvMapper.insertActivity(activity);
                    }
                });

        //Advantage insert
        cv.getAdvantages()
                .stream()
                .forEach(advantage ->
                {
                    advantage.setCv_no(cv.getCv_no());
                    advantage.setUser_id(cv.getUser_id());
                    log.info("CV DATA IS UPDATE(Advantage) : " + advantage.toString());
                    cvMapper.updateAdvantage(advantage);
                    if(advantage.getAdvantage_no() ==0){
                        log.info("기존에 작성된 우대사항이 없으므로 post 요청 보냅니다.");
                        cvMapper.insertAdvantage(advantage);
                    }
                });

        //Career insert
        cv.getCareers()
                .stream()
                .forEach(career ->
                {
                    career.setCv_no(cv.getCv_no());
                    career.setUser_id(cv.getUser_id());
                    log.info("CV DATA IS UPDATE(Career) : " + career.toString());
                    cvMapper.updateCareer(career);
                    if(career.getCareer_no() ==0){
                        log.info("기존에 작성된 경력사항이 없으므로 post 요청 보냅니다.");
                        cvMapper.insertCareer(career);
                    }
                });

        //Certification insert
        cv.getCertifications()
                .stream()
                .forEach(certification -> {
                    certification.setCv_no(cv.getCv_no());
                    certification.setUser_id(cv.getUser_id());
                    log.info("CV DATA IS UPDATE(certification) : " + certification.toString());
                    cvMapper.updateCertification(certification);
                    if(certification.getCert_no() ==0){
                        log.info("기존에 작성된 자격증이 없으므로 post 요청 보냅니다.");
                        cvMapper.insertCertification(certification);
                    }
                });

        //CVFile insert
        //cvMapper.insertCVFile();

        //Education insert
        cv.getEducations()
                .stream()
                .forEach(education ->
                {
                    education.setCv_no(cv.getCv_no());
                    education.setUser_id(cv.getUser_id());
                    log.info("CV DATA IS UPDATE(education) : " + education.toString());
                    cvMapper.updateEducation(education);
                });

        //Language insert
        cv.getLanguages()
                .stream()
                .forEach(language ->
                {
                    language.setCv_no(cv.getCv_no());
                    language.setUser_id(cv.getUser_id());
                    cvMapper.updateLanguage(language);
                    if(language.getLanguage_no() ==0){
                        log.info("기존에 작성된 어학성적이 없으므로 post 요청 보냅니다.");
                        cvMapper.insertLanguage(language);
                    }
                });

        //Skill insert
        cv.getSkills()
                .stream()
                .forEach(skill -> {
                    skill.setCv_no(cv.getCv_no());
                    skill.setUser_id(cv.getUser_id());
                    cvMapper.updateSkill(skill);
                    if(skill.getSkill_no() ==0){
                        log.info("기존에 작성된 보유기술이 없으므로 post 요청 보냅니다.");
                        cvMapper.insertSkill(skill);
                    }
                });
        return "";
    }

    @Override
    @Transactional
    public int create(CV cv) {

        //CV insert
        cvMapper.insertCV(cv);
        log.info("CV DATA IS : " + cv.toString());
        //Activity insert
        cv.getActivities()
                .stream()
                .forEach(activity -> {
                    activity.setCv_no(cv.getCv_no());
                    activity.setUser_id(cv.getUser_id());
                    cvMapper.insertActivity(activity);
                });

        //Advantage insert
        cv.getAdvantages()
                .stream()
                .forEach(advantage -> {
                    advantage.setCv_no(cv.getCv_no());
                    advantage.setUser_id(cv.getUser_id());
                    cvMapper.insertAdvantage(advantage);
                });

        //Career insert
        cv.getCareers()
                .stream()
                .forEach(career ->
                {
                    career.setCv_no(cv.getCv_no());
                    career.setUser_id(cv.getUser_id());
                    cvMapper.insertCareer(career);
                });

        //Certification insert
        cv.getCertifications()
                .stream()
                .forEach(certification -> {
                    certification.setCv_no(cv.getCv_no());
                    certification.setUser_id(cv.getUser_id());
                    cvMapper.insertCertification(certification);
                });

        //CVFile insert
        //cvMapper.insertCVFile();

        //Education insert
        cv.getEducations()
                .stream()
                .forEach(education ->
                {
                    education.setCv_no(cv.getCv_no());
                    education.setUser_id(cv.getUser_id());
                    cvMapper.insertEducation(education);
                });

        //Language insert
        cv.getLanguages()
                .stream()
                .forEach(language ->
                {
                    language.setCv_no(cv.getCv_no());
                    language.setUser_id(cv.getUser_id());
                    cvMapper.insertLanguage(language);
                });

        //Skill insert
        cv.getSkills()
                .stream()
                .forEach(skill -> {
                    skill.setCv_no(cv.getCv_no());
                    skill.setUser_id(cv.getUser_id());
                    cvMapper.insertSkill(skill);
                });

        return cv.getCv_no();
    }

    public List<CVFile> convertToCVFiles(MultipartFile[] files, String uploadPath, int cv_no, String user_id) {


        // UUID 생성 (파일 이름 중복을 방지하기 위함)
        String uuid = UUID.randomUUID().toString();

        List<CVFile> cvFileList = Arrays.stream(files)
                .map(file -> {
                    CVFile cvFile = new CVFile();
                    cvFile.setCv_no(cv_no);
                    cvFile.setUser_id(user_id);
                    cvFile.setFile_size((int) file.getSize());

                    //UUID FileName 저장
                    String originalFilename = file.getOriginalFilename();
                    String saveFileName = uuid + "_" + originalFilename;
                    cvFile.setFile_name(saveFileName);

                    // 확장자 추출
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    cvFile.setFile_type(fileExtension);

                    //경로 저장
                    String filePath = uploadPath + File.separator + saveFileName;
                    cvFile.setPath(filePath);

                    //업로드 날짜 저장
                    cvFile.setUpload_date(new Date(System.currentTimeMillis()));

                    try {
                        file.transferTo(new File(filePath));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return cvFile;
                })
                .collect(Collectors.toList());

        return cvFileList;
    }

    @Override
    public ResponseEntity<Object> crateFile(MultipartFile[] files, String endPath, int cv_no, String user_id) {

        try {
            // 원하는 경로
            String uploadPath = "C:\\pitch_resorces\\" + endPath;

            // 파일을 업로드할 경로 생성
            File folder = new File(uploadPath);
            if (!folder.exists()) {
                try {
                    if (folder.mkdirs()) {
                        System.out.println("폴더 생성 완료.");
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

            List<CVFile> cvFileList = convertToCVFiles(files, uploadPath,cv_no,user_id);

            log.info("BeforeFiles : " + cvFileList.toString());
            try {
                cvFileList
                        .stream()
                        .map((eachFile) -> {
                            log.info("Each Files : "+ eachFile.toString());
                            return cvMapper.insertCVFile(eachFile);
                        }).collect(Collectors.toList());
            }catch (Exception e){
                e.printStackTrace();
            }
            log.info("AfterFiles : " + cvFileList.toString());

            return ResponseEntity.ok("파일이 성공적으로 업로드되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류가 발생했습니다.");
        }
    }


}
