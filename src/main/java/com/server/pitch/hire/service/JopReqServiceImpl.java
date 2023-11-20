package com.server.pitch.hire.service;

import com.server.pitch.hire.domain.JobReq;
import com.server.pitch.hire.domain.ReqFile;
import com.server.pitch.hire.mapper.JobReqMapper;
import com.server.pitch.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class JopReqServiceImpl implements JobReqService {
    private JobReqMapper jobReqMapper;


    @Override
    public List<JobReq> findAll(String user_id) {
        return jobReqMapper.selectJobReqList(user_id);
    }

    @Override
    public List<JobReq> findAllByStatus(List<String> selectedStatus, String user_id) {
        return jobReqMapper.selectJobReqStatusList(selectedStatus, user_id);
    }

    @Override
    public JobReq findByNo(int job_req_no) {
        return jobReqMapper.selectJobReq(job_req_no);
    }

    @Override
    public int createJobReq(JobReq jobReq) {
        jobReqMapper.insertJobReq(jobReq);
        return jobReq.getJob_req_no();
    }

    @Override
    public void deleteJobReq(int job_req_no) {
        jobReqMapper.deleteJobReq(job_req_no);
    }

    @Override
    public void deleteJobReqList(List<Integer> jobReqNo) {
        jobReqMapper.deleteJobReqList(jobReqNo);
    }

    @Override
    public void modifyJobReq(JobReq jobReq) {
        jobReqMapper.updateJobReq(jobReq);
    }

    @Override
    public void modifyStatusList(List<JobReq> jobReqs) {
        for (JobReq jobReq : jobReqs) {
            jobReqMapper.updateStatusList(jobReq);
        }
    }

    @Override
    public List<JobReq> combinedSearchByThings(Map<String, Object> params) {
        //log.info(jobReqMapper.combinedSearchByThings(params).toString());
        return jobReqMapper.combinedSearchByThings(params);
    }

    @Override
    public List<Users> findUserWithDept() {
        return jobReqMapper.selectUserWithDept();
    }

    @Override
    public void addInterviewersToJobReq(JobReq jobReq, List<String> interviewer_id){
        jobReqMapper.updateJobReq(jobReq);

        try {
            for(String id : interviewer_id) {
                jobReqMapper.insertInterviewer(jobReq.getJob_req_no(), id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void createReqFiles(List<MultipartFile> files, int jobReqNo) throws IOException {
        for (MultipartFile file : files) {
            // 파일 정보 추출
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileType = file.getContentType();
            long fileSize = file.getSize();

            // 파일 저장 경로 설정
            String uploadDir = "C:\\pitch";
            String path = uploadDir + "\\" + fileName;

            // 파일 저장
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            file.transferTo(new File(path));

            // 데이터베이스에 파일 정보 저장
            ReqFile reqFile = new ReqFile();
            reqFile.setJob_req_no(jobReqNo);
            reqFile.setFile_name(fileName);
            reqFile.setFile_type(fileType);
            reqFile.setFile_size((int) fileSize);
            reqFile.setUpload_date(new Date());
            reqFile.setPath(path);

            jobReqMapper.insertReqFile(reqFile);
        }
    }

    @Override
    public List<ReqFile> findReqFiles(int jobReqNo) {
        return jobReqMapper.selectReqFiles(jobReqNo);

    }

    @Override
    public ReqFile selectReqFilesByFileNo(int reqfile_no) {
        return jobReqMapper.selectReqFilesByFileNo(reqfile_no);
    }

    @Override
    public ResponseEntity<Resource> downloadFile(int reqFileNo) {
        ReqFile reqFile = jobReqMapper.selectReqFilesByFileNo(reqFileNo);

        if (reqFile != null) {
            // 파일 경로
            String filePath = reqFile.getPath();

            // 파일을 Resource로 읽어오기
            Resource resource = loadFileAsResource(filePath);

            // Content-Disposition 헤더 설정
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }

        // 파일이 없을 경우 NOT_FOUND 반환
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    private Resource loadFileAsResource(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not read the file!", ex);
        }
    }



//    @Override
//    public Resource downloadFile(String file_name) {
//        ReqFile reqFile = jobReqMapper.selectReqFiles(jobReqNo);
//        try {
//
//            Path filePath = Paths.get(uploadPath, attached_name); // 파일이 저장된 실제 경로로 변경
//            Resource resource = new UrlResource(filePath.toUri());
//            if (resource.exists()) {
//                return resource;
//            } else {
//                throw new FileNotFoundException("File not found: " + attached_name);
//            }
//        } catch (MalformedURLException | FileNotFoundException ex) {
//            throw new RuntimeException("File not found: " + attached_name, ex);
//        }
//    }



}
