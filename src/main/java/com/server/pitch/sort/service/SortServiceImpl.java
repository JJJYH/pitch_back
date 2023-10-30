package com.server.pitch.sort.service;

import com.server.pitch.sort.config.EmailConfig;
import com.server.pitch.sort.domain.ApplicantDetailResponse;
import com.server.pitch.sort.domain.ApplicantRequest;
import com.server.pitch.sort.domain.ApplicantResponse;
import com.server.pitch.sort.domain.PostingInfoResponse;
import com.server.pitch.sort.mapper.SortMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class SortServiceImpl implements SortService{
    @Autowired
    SortMapper mapper;

    @Autowired
    EmailConfig emailConfig;

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public List<ApplicantResponse> findAll(ApplicantRequest req) {
        return mapper.selectSortList(req);
    }

    @Override
    public ApplicantDetailResponse findOne(int applyNo) {
        return mapper.selectApplicant(applyNo);
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

        if ("pass".equals(type)) {
            switch (statusType) {
                case "서류전형":
                    newStatus = "second";
                    status = "F";
                    break;
                case "면접전형":
                    newStatus = "final";
                    status = "S";
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

        sendMail(list, statusType, (String)data.get("contents"), title);

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
                    "이름", row.getUser_nm(),
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
                    message.setText(template);

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
}
