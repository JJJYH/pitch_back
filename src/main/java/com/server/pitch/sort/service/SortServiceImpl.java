package com.server.pitch.sort.service;

import com.server.pitch.cv.domain.CV;
import com.server.pitch.cv.domain.CVFile;
import com.server.pitch.sort.config.EmailConfig;
import com.server.pitch.sort.domain.*;
import com.server.pitch.sort.mapper.SortMapper;
import com.server.pitch.sort.utils.CVtoExel;
import com.server.pitch.sort.utils.ScoreCalculator;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Log4j2
public class SortServiceImpl implements SortService{
    @Autowired
    SortMapper mapper;

    @Autowired
    EmailConfig emailConfig;

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    Komoran nlp = null;

    public SortServiceImpl() {
        this.nlp = new Komoran(DEFAULT_MODEL.LIGHT);
    }

    @Override
    public List<String> doWordNouns(String text) throws Exception {

        String replace_text = text.replace("[^가-힣a-zA-Z0-9", " ");
        String trim_text = replace_text.trim();


        KomoranResult analyzeResultList = this.nlp.analyze(trim_text);
        List<String> rList = analyzeResultList.getNouns();

        if (rList == null) {
            rList = new ArrayList<String>();
        }

        Iterator<String> it = rList.iterator();

        while (it.hasNext()) {
            String word = it.next();
        }

        return rList;
    }

    @Override
    public List<Map<String, Object>> doWordCount(List<String> pList) throws Exception {

        if (pList ==null) {
            pList = new ArrayList<String>();
        }


        Set<String> rSet = new HashSet<String>(pList);
        Iterator<String> it = rSet.iterator();
        List<Map<String, Object>> rList = new ArrayList<>();

        while(it.hasNext()) {
            Map<String, Object> rMap = new HashMap<>();
            String word = it.next();
            int frequency = Collections.frequency(pList, word);

            rMap.put("text", word);
            rMap.put("value", frequency);
            rList.add(rMap);
        }

        return rList;
    }

    @Override
    public List<Map<String, Object>> doWordAnalysis(String text) throws Exception {

        List<String> pList = this.doWordNouns(text);

        if(pList == null) {
            pList = new ArrayList<String>();
        }

        List<Map<String, Object>> rList = this.doWordCount(pList);

        if(rList == null) {
            rList = new ArrayList<>();
        }

        return rList;
    }

    @Override
    public Score selectScore(int job_posting_no, int apply_no) {
        return mapper.selectScore(job_posting_no, apply_no);
    }

    @Override
    public ApplicantAvgResponse selectAvg(int job_posting_no) {
        return mapper.selectAvg(job_posting_no);
    }

    @Override
    public byte[] cvToExcel(List<Integer> list) {
        for(Integer applyNo : list) {
            try {
                ApplicantDetailResponse applicant = mapper.selectApplicant(applyNo);
                encodePicture(applicant);
                return CVtoExel.copyExcelTemplate(applicant);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public byte[] downloadFiles(FileDownloadRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {

            for(Integer applyNo : req.getApplyNo()) {
                ApplicantDetailResponse applicant = mapper.selectApplicant(applyNo);
                if(applicant.getCv().getCvFiles().get(0).getFile_name() == null) break;

                for(String type : req.getType()) {
                    if(type.equals("CV")) {
                        encodePicture(applicant);
                        byte[] cv = CVtoExel.copyExcelTemplate(applicant);

                        String path = applicant.getCv().getUser_nm() +
                                File.separator + "입사지원서(" + applicant.getCv().getUser_nm() + ").xlsx";
                        zipOut.putNextEntry(new ZipEntry(path));
                        zipOut.write(cv, 0, cv.length);
                        zipOut.closeEntry();
                    } else {
                        for(CVFile file : applicant.getCv().getCvFiles()) {
                            String fileType = "";
                            if(type.equals("Portfolio")) {
                                fileType = "포트폴리오";
                            } else if(type.equals("Statement")) {
                                fileType = "자기소개서";
                            } else if(type.equals("etcDocs")) {
                                fileType = "기타문서";
                            }  else if(type.equals("Career")) {
                                fileType = "경력기술서";
                            }
                            if(file.getType().equals(type)) {
                                String path = applicant.getCv().getUser_nm() +
                                        File.separator + fileType + "(" + applicant.getCv().getUser_nm() + ")." + file.getFile_type();
                                zipOut.putNextEntry(new ZipEntry(path));

                                File newFile = new File(file.getPath());

                                InputStream inputStream = Files.newInputStream(newFile.toPath());
                                byte[] buffer = new byte[16384];
                                int len;
                                while ((len = inputStream.read(buffer)) > 0) {
                                    zipOut.write(buffer, 0, len);
                                }
                                inputStream.close();
                                zipOut.closeEntry();
                            }
                        }
                    }

                }
            }
            zipOut.finish();

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] downloadFile(FileDownloadRequest req) {
        int applyNo = req.getApplyNo().get(0);
        String type = req.getType().get(0);
        ApplicantDetailResponse applicant = mapper.selectApplicant(applyNo);

        for(CVFile file : applicant.getCv().getCvFiles()) {
            if(file.getType().equals(type)) {
                File newFile = new File(file.getPath());

                try (FileInputStream fis = new FileInputStream(newFile)) {
                    byte[] bytesArray = new byte[(int) newFile.length()];
                    fis.read(bytesArray);
                    return bytesArray;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new byte[0];
    }

    @Override
    public List<Map<String, Object>> createWordClouds(int apply_no) {
        String filePath = "C:\\pitch_resorces\\더존ICT그룹 입사지원서-(성명)_v7.3.xlsx";
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int rowindex = 0;
        int columnindex = 0;

        Sheet sheet = workbook.getSheetAt(2);

        int rows = (sheet.getLastRowNum() + 1);
        int maxCells = 0;
        for (rowindex = 0; rowindex < rows; rowindex++) {
            Row row = sheet.getRow(rowindex);
            if (row != null) {
                int cells = (row.getLastCellNum());
                if (cells > maxCells)
                    maxCells = cells;
            }

        }

        String[][] merge = new String[rows][maxCells];
        for (int i = 0; i < sheet.getNumMergedRegions(); ++i) {
            CellRangeAddress range = sheet.getMergedRegion(i);

            int mergeRow = range.getFirstRow();
            int mergeCol = range.getFirstColumn();
            int rowLength = range.getLastRow() - range.getFirstRow() + 1;
            int colLength = range.getLastColumn() - range.getFirstColumn() + 1;

            for (int r = 0; r < rowLength; r++) {
                for (int c = 0; c < colLength; c++) {

                    if (r == 0 && c == 0) {
                        merge[mergeRow][mergeCol] = rowLength + "," + colLength;
                    } else {
                        merge[mergeRow + r][mergeCol + c] = "mergeCell";
                    }

                }
            }
        }

        String[][] text = new String[rows][maxCells];
        for (rowindex = 0; rowindex < rows; rowindex++) {

            Row row = sheet.getRow(rowindex);
            if (row != null) {
                int cells = row.getLastCellNum();
                for (columnindex = 0; columnindex <= cells; columnindex++) {

                    Cell cell = row.getCell(columnindex);

                    String value = "";

                    if (cell == null) {
                        continue;
                    } else {
                        switch (cell.getCellTypeEnum()) {
                            case FORMULA:
                                value = cell.getCellFormula();
                                break;
                            case NUMERIC:
                                value = cell.getNumericCellValue() + "";
                                break;
                            case STRING:
                                value = cell.getStringCellValue() + "";
                                break;
                            case BLANK:
                                value = "";
                                break;
                            case BOOLEAN:
                                value = cell.getBooleanCellValue() + "";
                                break;
                            case ERROR:
                                value = cell.getErrorCellValue() + "";
                                break;
                            default:
                                value = "Unknown Cell Type";
                                break;
                        }
                    }
                    text[rowindex][columnindex] = value;
                }

            }
        }
        List<Map<String, Object>> rMap = null;
        try {
            rMap = doWordAnalysis(Arrays.deepToString(text));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (rMap == null) {
            rMap = new ArrayList<>();
        }

        return rMap;
    }

    @Override
    public List<ApplicantResponse> findAll(ApplicantRequest req) {
        List<ApplicantResponse> res = mapper.selectSortList(req);
        String path = "C:" +File.separator +  "pitch_resorces" + File.separator +  "images" + File.separator;

        for(ApplicantResponse person : res) {
            try {
                String fileName = person.getPicture();

                File file = new File(path + fileName);

                byte[] byteFile = FileCopyUtils.copyToByteArray(file);
                byte[] base64 =
                        Base64.getEncoder().encode(byteFile);

                person.setPicture(new String(base64));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    @Override
    public ApplicantDetailResponse findOne(int applyNo) {
        ApplicantDetailResponse person = mapper.selectApplicant(applyNo);
        FilterRequest filter = new FilterRequest();
        ScoreCalculator.calculateScore(filter, person);
        encodePicture(person);

        return person;
    }

    @Override
    public List<ApplicantDetailResponse> findAllFilteredApplicant(int postingNo, FilterRequest filter) {
        List<ApplicantDetailResponse> list = mapper.selectFilteredApplicant(postingNo);

        for(ApplicantDetailResponse applicant : list) {

            ScoreCalculator.calculateScore(filter, applicant);
            encodePicture(applicant);
        }

        return list;
    }

    @Override
    public String statusTypeUpdate(List<Map<String, Object>> data) {
        data.forEach(row -> {
            try {
                mapper.updateStatus(row);
            } catch (Exception e) {
                throw new RuntimeException("쿼리 실행 중 오류 발생", e);
            }
        });
        return null;
    }

    @Override
    public String statusUpdate(Map<String, Object> data) {
        String type = (String) data.get("type");
        String statusType = (String) data.get("status_type");
        String title = (String) data.get("title");
        String status = "";
        String newStatus = "";
        String newStatusType = "평가전";
        String contents = (String)data.get("contents");

        if ("pass".equals(type)) {
            switch (statusType) {
                case "서류전형":
                    contents = contents.replaceAll("\\n", "<br>");
                    contents = "<h3>" + contents + "</h3>";
                    String text = "<a href='https://work.go.kr/consltJobCarpa/jobPsyExam/" +
                            "guest/univJobPsyExamInfo.do?psyExamOrgcd=B0236&psyExamStudentId=1001' " +
                            "style='display: inline-block; width: 150px; height: 40px; padding: 15px 30px;" +
                            "background-color: #38678f; text-align: center;  margin-top: 50px;" +
                            "text-decoration: none; color: white; border-radius: 4px;'><b>인적성검사 하러가기</b></a>";
                    contents += text;
                    newStatus = "final";
                    status = "F";
                    break;
                case "최종합격":
                    newStatus = "final";
                    newStatusType = "최종합격";
                    status = "FL";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid statusType: " + statusType);
            }
        } else if ("fail".equals(type)) {
            switch (statusType) {
                case "서류전형":
                    status = "F";
                    break;
                case "최종합격":
                    status = "FL";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid statusType: " + statusType);
            }
            newStatus = "finished";
            newStatusType = statusType.substring(0, 2) + " 불합격";
        } else {
            throw new IllegalArgumentException("Invalid type: " + type);
        }

        ApplicantRequest req = new ApplicantRequest(
                Integer.parseInt((String)data.get("job_posting_no")),
                type,
                status
        );

        List<ApplicantResponse> list = mapper.selectSortList(req);

        sendMail(list, statusType, contents, title);

        for(ApplicantResponse row : list) {
            Map<String, Object> map = new HashMap<>();

            map.put("applicant_status", newStatus);
            map.put("status_type", newStatusType);
            map.put("apply_no", row.getApply_no());

            try {
                mapper.updateStatus(map);
            } catch (Exception e) {
                throw new RuntimeException("쿼리 실행 중 오류 발생", e);
            }
        };

        return null;
    }

    @Override
    public PostingInfoResponse findInfoByPostingNo(int postingNo) {
        return mapper.selectPostingInfo(postingNo);
    }

    @Override
    public void createEval(CandidateEval eval) {
        mapper.insertEval(eval);
    }

    public void sendMail(List<ApplicantResponse> list, String statusType, String contents, String title) {

        String companyName = "더존비즈온";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", emailConfig.getSmtpHost());
        props.put("mail.smtp.port", emailConfig.getSmtpPort());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", emailConfig.getSmtpHost());

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailConfig.getUserEmail(), emailConfig.getUserPassword());
                    }
                });

        for(ApplicantResponse row : list) {
            Map<String, String> variables = Map.of(
                    "이름", "<span style='color: #38678f;'>" + row.getUser_nm() + "</span>",
                    "회사명", companyName,
                    "공고명", title
            );
            final String template = replaceVariables(contents, variables);

            executorService.execute(() -> {
                try {

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(emailConfig.getUserEmail()));

                    message.setRecipients(
                            Message.RecipientType.TO,
                            InternetAddress.parse(row.getUser_email())
                    );

                    message.setSubject("[" + companyName +"]" + statusType + "발표 안내드립니다.");
//                    message.setText(template);
                    message.setContent(template, "text/html; charset=utf-8");
                    Transport.send(message);

                } catch (MessagingException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            });
        }
    }

    public String replaceVariables(String template, Map<String, String> variables) {
        Pattern pattern = Pattern.compile("%(.*?)%");
        Matcher matcher = pattern.matcher(template);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String variableName = matcher.group(1);
            String variableValue = variables.get(variableName);
            if (variableValue != null) {
                matcher.appendReplacement(buffer, variableValue);
            }
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public void encodePicture(ApplicantDetailResponse person) {
        String path = "C:" +File.separator +  "pitch_resorces" + File.separator +  "images" + File.separator;

        try {
            List<CVFile> files = person.getCv().getCvFiles();
            String fileName = "";
            for(CVFile file : files) {
                if(file.getType() == null) break;
                if(file.getType().equals("images")) {
                    fileName = file.getFile_name();
                }
            }

            if (fileName.isEmpty()) {
                person.setPicture(null);
            } else {
                File file = new File(path + fileName);
                byte[] byteFile = FileCopyUtils.copyToByteArray(file);
                byte[] base64 = Base64.getEncoder().encode(byteFile);
                person.setPicture(new String(base64));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void createScore(int apply_no, CV cv) {
        Score score = new Score();
        int postingNo = cv.getJob_posting_no();
        score.setApply_no(apply_no);

        FilterRequest filter = mapper.selectFilter(postingNo);

        filter.setAdvantageScore(5);
        filter.setCareerScore(30);
        filter.setCertificationScore(15);
        filter.setEducationScore(30);
        filter.setLanguageScore(20);

        ScoreCalculator.calculateScore(filter, score, cv);
        mapper.insertScore(score);
        mapper.updateScore(apply_no, score.getScore());
    }

    public void testScore(int applyNo, ApplicantDetailResponse applicant) {

        int postingNo = applicant.getCv().getJob_posting_no();
        FilterRequest filter = mapper.selectFilter(postingNo);
        filter.setAdvantageScore(5);
        filter.setCareerScore(30);
        filter.setCertificationScore(15);
        filter.setEducationScore(30);
        filter.setLanguageScore(20);
        ScoreCalculator.calculateScore(filter, applicant);
    }

    @Override
    public void readStatusUpdate(int applyNo) {
        mapper.updateReadStatus(applyNo);
    }
}
