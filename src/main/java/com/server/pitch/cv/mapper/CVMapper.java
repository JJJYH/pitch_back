package com.server.pitch.cv.mapper;

import com.server.pitch.cv.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CVMapper {
    public CV selectCVList(CV cv);
    public int selectCVNO (CV cv);

    //========================CV Create Part===========================
    public int insertCV(CV cv);
    public int insertActivity(Activity activity);
    public int insertAdvantage(Advantage advantage);
    public int insertCareer(Career career);
    public int insertCertification(Certification certification);
    public int insertCVFile(CVFile cvFile);
    public int insertEducation(Education education);
    public int insertLanguage(Language language);
    public int insertSkill(Skill skill);

//    UPDATE MAPPER
    public int updateCV(CV cv);
    public int updateActivity(Activity activity);
    public int updateAdvantage(Advantage advantage);
    public int updateCareer(Career career);
    public int updateCertification(Certification certification);
    public int updateCVFile(CVFile cvFile);
    public int updateEducation(Education education);
    public int updateLanguage(Language language);
    public int updateSkill(Skill skill);
}
