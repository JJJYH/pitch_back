package com.server.pitch.cv.mapper;

import com.server.pitch.cv.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CVMapper {
    public CV selectCVList();

    //========================CV Create Part===========================
    public int insertCV(CV cv);
    public int insertActivity(Activity activity);
    public int insertAdvantage(Advantage advantage);
    public int insertCareer(Career career);
    public int insertCertification(Certification certification);
    public int insertCVFile();
    public int insertEducation(Education education);
    public int insertLanguage(Language language);
    public int insertSkill(Skill skill);

}
