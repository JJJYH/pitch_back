package com.server.pitch.cv.service;

import com.server.pitch.cv.domain.*;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
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
    public List<CVFile> findCVFile(int cv_no, String user_id) {
        return cvMapper.selectCVFile(cv_no, user_id);
    }

    @Override
    public String findPosition(int job_posting_no) {
        return cvMapper.selectPosition(job_posting_no);
    }

    @Override
    public List<Apply> findApplyList(String user_id) {
        return cvMapper.selectApplyList(user_id);
    }

    @Override
    public List<JobReq> findJobInfoList(int job_posting_no) {
        return cvMapper.selectJobInfo(job_posting_no);
    }

    @Override
    public ResponseEntity<Object> createImageFile(CVFile imgCVFile) {
        log.info("ImgCVFile Is : "+imgCVFile);
        cvMapper.insertCVFile(imgCVFile);
        return ResponseEntity.ok("이미지 파일이 성공적으로 업로드되었습니다.");
    }

    @Override
    public int findCVNO(CV cv) {
        Integer result = cvMapper.selectCVNO(cv);

        if (result != null) {
            int intValue = result;
            return intValue;
        } else {
            // 결과가 null일 때 처리
            int intValue = -1;
            return intValue;
        }
    }

    @Override
    public int findMainCVNO(CV cv) {
        Integer result = cvMapper.selectMainCVNO(cv);

        if (result != null) {
            int intValue = result;
            return intValue;
        } else {
            // 결과가 null일 때 처리
            int intValue = -1;
            return intValue;
        }
    }

    /*update 요청 시 기존 DB 데이터와 비교 후, 삭제된 컴포넌트 DELETE MAPPING*/
//    public void removeCompModify(CV cv){
//        CV beforeCV = cvMapper.selectCVList(cv);
//
//        /**Activity Remove Process*/
//        List<Activity> beforeActivity = new ArrayList<>();
//        List<Activity> updateActivity = new ArrayList<>();
//
//        beforeActivity = beforeCV.getActivities();
//        updateActivity = cv.getActivities();
//
//        List<Activity> resultActivity = updateActivity;
//        //기존에 DB에만 있는 데이터만 남기도록 하는 프로세스
//        beforeActivity
//                .removeIf(beforeAct -> resultActivity
//                        .stream()
//                        .anyMatch(updateAct-> updateAct.getActivity_no() == beforeAct.getActivity_no()));
//
//        /**Advantage Remove Process*/
//        List<Advantage> beforeAdvantage = new ArrayList<>();
//        List<Advantage> updateAdvantage = new ArrayList<>();
//        beforeAdvantage = beforeCV.getAdvantages();
//        updateAdvantage = cv.getAdvantages();
//
//        List<Advantage> resultAdvantage = updateAdvantage;
//
//        /**Career Remove Process*/
//        List<Career> beforeCareer = new ArrayList<>();
//        List<Career> updateCareer = new ArrayList<>();
//        beforeCareer = beforeCV.getCareers();
//        updateCareer = cv.getCareers();
//
//        List<Career> resultCareer = updateCareer;
//
//        /**Certification Remove Process*/
//        List<Certification> beforeCert = new ArrayList<>();
//        List<Certification> updateCert = new ArrayList<>();
//        beforeCert = beforeCV.getCertifications();
//        updateCert = cv.getCertifications();
//
//        List<Certification> resultCert = updateCert;
//
//        /**Education Remove Process*/
//        List<Education> beforeEducation = new ArrayList<>();
//        List<Education> updateEducation = new ArrayList<>();
//        beforeEducation = beforeCV.getEducations();
//        updateEducation = cv.getEducations();
//
//        List<Education> resultEducation = updateEducation;
//
//        /**Language Remove Process*/
//        List<Language> beforeLanguage = new ArrayList<>();
//        List<Language> updateLanguage = new ArrayList<>();
//        beforeLanguage = beforeCV.getLanguages();
//        updateLanguage = cv.getLanguages();
//
//        List<Language> resultLanguage = updateLanguage;
//
//        /**Skill Remove Process*/
//        List<Skill> beforeSkill = new ArrayList<>();
//        List<Skill> updateSkill = new ArrayList<>();
//        beforeSkill = beforeCV.getSkills();
//        updateSkill = cv.getSkills();
//
//        List<Skill> resultSkill = updateSkill;
//
//        /**CVFile Remove Process*/
//        List<CVFile> beforeCVFile = new ArrayList<>();
//        List<CVFile> updateCVFile = new ArrayList<>();
//        beforeCVFile = beforeCV.getCvFiles();
//        updateCVFile = cv.getCvFiles();
//
//        List<CVFile> resultCVFile = updateCVFile;
//
//
//
//
//    }

    public void removeCompModify(CV cv){
        CV beforeCV = cvMapper.selectCVList(cv);
        log.info("BEFORE CV IS : " + beforeCV);
        log.info("UPDATE CV IS : " + cv);
        /**Activity Remove Process*/

                removeCompModifyList(beforeCV.getActivities(), cv.getActivities(), Activity::getActivity_no)
                        .forEach(item -> cvMapper.deleteActivity(item.getActivity_no()));



        /**Advantage Remove Process*/

                removeCompModifyList(beforeCV.getAdvantages(), cv.getAdvantages(), Advantage::getAdvantage_no)
                        .forEach(item -> cvMapper.deleteAdvantage(item.getAdvantage_no()));



        /**Career Remove Process*/

                removeCompModifyList(beforeCV.getCareers(), cv.getCareers(), Career::getCareer_no)
                        .forEach(item -> cvMapper.deleteCareer(item.getCareer_no()));


        /**Certification Remove Process*/

                removeCompModifyList(beforeCV.getCertifications(), cv.getCertifications(), Certification::getCert_no)
                        .forEach(item -> cvMapper.deleteCertification(item.getCert_no()));



        /**Education Remove Process*/

                removeCompModifyList(beforeCV.getEducations(), cv.getEducations(), Education::getEdu_no)
                        .forEach(item -> cvMapper.deleteEducation(item.getEdu_no()));



        /**Language Remove Process*/

                removeCompModifyList(beforeCV.getLanguages(), cv.getLanguages(), Language::getLanguage_no)
                        .forEach(item -> cvMapper.deleteLanguage(item.getLanguage_no()));



        /**Skill Remove Process*/

                removeCompModifyList(beforeCV.getSkills(), cv.getSkills(), Skill::getSkill_no)
                        .forEach(item -> cvMapper.deleteSkill(item.getSkill_no()));



        /**CVFile Remove Process*/
                    if(!"MainCV".equals(cv.getCv_status())){
                        removeCompModifyList(beforeCV.getCvFiles(), cv.getCvFiles(), CVFile::getCv_file_no)
                                .forEach(item -> {
                                    removeRealFile(item.getPath());
                                    cvMapper.deleteCVFile(item.getCv_file_no());
                                });
                    }
    }

    public void removeRealFile(String path){
        if (path == null) {
            log.warn("파일 경로가 null입니다.");
            return;
        }
        File file = new File(path);
        if (file.exists()){
            //파일 삭제
            log.info("해당 경로의 파일을 삭제합니다 : "+ file.getName());
            Path filePath = Paths.get(path);
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private <T, ID> List<T> removeCompModifyList(List<T> beforeList, List<T> updateList, Function<T, ID> idExtractor) {

        log.info("UPDATE LIST IS : " + updateList);

        List<T> resultList = new ArrayList<>(updateList != null ? updateList : Collections.emptyList());
        log.info("RESULT LIST IS : " + resultList);


            beforeList
                    .removeIf(beforeItem ->
                            resultList
                                    .stream()
                                    .anyMatch(updateItem ->
                                            Objects.equals(idExtractor.apply(updateItem), idExtractor.apply(beforeItem))
                                    )
                    );
            log.info("CALCULATED IS : "+ beforeList);
        List<T> modifiedList = new ArrayList<>(beforeList);

        return modifiedList;
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
                    if(certification.getCert_no() == 0){
                        log.info("기존에 작성된 자격증이 없으므로 post 요청 보냅니다.");
                        cvMapper.insertCertification(certification);
                    }
                });

        //Education insert
        cv.getEducations()
                .stream()
                .forEach(education ->
                {
                    education.setCv_no(cv.getCv_no());
                    education.setUser_id(cv.getUser_id());
                    cvMapper.updateEducation(education);
                    if(education.getEdu_no() ==0) {
                        log.info("기존에 작성된 학력이 없으므로 post 요청 보냅니다.");
                        cvMapper.insertEducation(education);
                    }
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

        removeCompModify(cv);
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

    public List<CVFile> convertToCVFiles(MultipartFile[] files, String uploadPath, int cv_no, String user_id, String endPath) {
        // UUID 생성 (파일 이름 중복을 방지하기 위함)
        String uuid = UUID.randomUUID().toString();

        List<CVFile> cvFileList = Arrays.stream(files)
                .map(file -> {
                    CVFile cvFile = new CVFile();
                    cvFile.setCv_no(cv_no);
                    cvFile.setUser_id(user_id);
                    cvFile.setFile_size((int) file.getSize());
                    cvFile.setType(endPath);

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
            List<CVFile> cvFileList = convertToCVFiles(files, uploadPath,cv_no,user_id, endPath);

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

    @Override
    public int createApply(CV cv, int apply_no) {
           log.info("CREATE APPLY IS : " + cv);
        return cvMapper.insertApply(cv, apply_no);
    }
}
