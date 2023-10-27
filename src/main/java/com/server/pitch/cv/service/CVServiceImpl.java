package com.server.pitch.cv.service;

import com.server.pitch.cv.domain.Activity;
import com.server.pitch.cv.domain.CV;
import com.server.pitch.cv.mapper.CVMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CVServiceImpl implements CVService {

    @Autowired
    CVMapper cvMapper;

    @Override
    public CV findAll() {
        return cvMapper.selectCVList();
    }

    @Override
    @Transactional
    public String create(CV cv) {

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

        return "";
    }
}
