package com.server.pitch.cv.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.pitch.aop.GetUserAccessToken;
import com.server.pitch.cv.domain.CV;
import com.server.pitch.cv.domain.CVFile;
import com.server.pitch.cv.domain.ChartData;
import com.server.pitch.cv.service.CVService;
import com.server.pitch.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
@RequestMapping("/admin/main/cv")
public class
CVController {

    @Autowired
    public CVService cvService;
    @GetUserAccessToken
    @PostMapping("/image-upload")
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
        log.info("FIND ALL CV : "+ cvService.findAll(cv));
        return ResponseEntity.ok(cvService.findAll(cv));
    }

    private void addFileToZip(ZipOutputStream zipOut, String filename, String content) throws IOException {
        zipOut.putNextEntry(new ZipEntry(filename));
        zipOut.write(content.getBytes());
        zipOut.closeEntry();
    }
    @GetUserAccessToken
    @GetMapping("/apply-list")
    public ResponseEntity<Object> getApplyList(Users loginUser){
        Object result = cvService.findApplyList(loginUser.getUser_id());
        log.info("APPLY LIST IS : "+ result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/job-info-list")
    public ResponseEntity<Object> getJobInfoList(@RequestParam("job_posting_no") int job_posting_no){
        return ResponseEntity.ok().body(cvService.findJobInfoList(job_posting_no));
    }
    @GetUserAccessToken
    @GetMapping("/get-files")
    public ResponseEntity<byte[]> getFile(Users loginUSer,@RequestParam("cv_no")int cv_no){

        List<CVFile> getfiles = cvService.findCVFile(cv_no, loginUSer.getUser_id());
        log.info("GET FILES : " + getfiles );

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            for (CVFile cvFile : getfiles) {
                //Resource resource = new ClassPathResource(cvFile.getPath());
                File file = new File(cvFile.getPath());

                //존재하는 파일인 경우에만 zip 생성
                if(file.exists()){
                    InputStream inputStream = Files.newInputStream(file.toPath());

                    // 파일명을 지정하여 ZipEntry 생성
                    ZipEntry zipEntry = new ZipEntry(cvFile.getFile_name());
                    zipOutputStream.putNextEntry(zipEntry);

                    // 파일 데이터를 ZipOutputStream에 쓰기
                    byte[] buffer = new byte[16384];
                    int len;
                    while ((len = inputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                    zipOutputStream.closeEntry();
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 압축된 파일 데이터를 byte 배열로 변환하여 클라이언트에게 전송
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "files.zip");

        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @GetUserAccessToken
    @GetMapping("get-files-infos")
    public ResponseEntity<Object> getFileInfos(Users loginUSer, @RequestParam("cv_no")int cv_no){

        List<CVFile> getfiles = cvService.findCVFile(cv_no, loginUSer.getUser_id());
        log.info("GET FILES : " + getfiles );

        getfiles.forEach(
                file ->{
                    String name[] = file.getFile_name().split("_");
                    //Origin Name Setting
                    file.setFile_name(name[1]);
                }
        );

        return ResponseEntity.ok().body(getfiles);
    }

    @GetMapping("/find-position")
    public ResponseEntity<Object> getPosition(@RequestParam("job_posting_no") int job_posting_no){
        String position = cvService.findPosition(job_posting_no);
        return ResponseEntity.ok(position);
    }

    @GetMapping("/apply")
    public ResponseEntity<Object> applyCheck(@RequestParam("cv_no")int cv_no){
        int applyChecking = cvService.findApplyCheck(cv_no);
        return ResponseEntity.ok().body(applyChecking);
    }
    @GetMapping("/req-count")
    public ResponseEntity<Object> reqCount(@RequestParam("job_posting_no")int job_posting_no, @RequestParam("cv_no")int cv_no){
        log.info("Data : " + job_posting_no + "CV_NO : " +cv_no);
                List<ChartData> result = cvService.findCountReq(job_posting_no);
                result.addAll(cvService.findCountReqUser(cv_no));
        log.info("Result : "+result);

        return ResponseEntity.ok().body(result);
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
    @GetUserAccessToken
    @GetMapping("/get-main-cv-no")
    public ResponseEntity<Object> getMainCVNO(Users loginUSer){

        CV cv = new CV();
        cv.setUser_id(loginUSer.getUser_id());
        cv.setCv_status("MainCV");
        log.info("GET MAIN CV IS : "+ cv);
        int result = cvService.findMainCVNO(cv);
        log.info("GET MAIN CV NO IS : " + result);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Integer> CVCrate(@RequestBody Map<String,Object> requestBody){
        log.info("Req Data : "+requestBody.toString());
        ObjectMapper mapper = new ObjectMapper();
        CV cv = mapper.convertValue(requestBody.get("cv"),CV.class);
        log.info("CV Data: " + cv);
        int cv_no = cvService.create(cv);
//        int cv_no = 119;
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
    @PostMapping("/cv-file-upload")
    public ResponseEntity<Object> CVFileUpload(Users loginUSer,@RequestParam("cvfile") MultipartFile[] files, @RequestParam("endPath") String endPath,@RequestParam("cv_no")int cv_no) {
        return cvService.crateFile(files,endPath,cv_no, loginUSer.getUser_id());
    }

    @PostMapping("/send-apply")
    public ResponseEntity<Object> CVApply(@RequestBody Map<String,Object> requestBody){
        ObjectMapper mapper = new ObjectMapper();
        CV cv = mapper.convertValue(requestBody.get("cv"),CV.class);
        log.info("SEND APPLY DATA IS : " + cv);
        int apply_no = 0;
        cvService.createApply(cv, apply_no);
        return ResponseEntity.ok("Send OK");
    }

}
