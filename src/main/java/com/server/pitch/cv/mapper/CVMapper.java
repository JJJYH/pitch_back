package com.server.pitch.cv.mapper;

import com.server.pitch.cv.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CVMapper {

    // SELECT MAPPER
    public CV selectCVList(CV cv);
    public Integer selectCVNO (CV cv);
    public Integer selectMainCVNO(CV cv);
    public String selectPosition(int job_posting_no);
    public List<CVFile> selectCVFile(int cv_no, String user_id);
    public List<Apply> selectApplyList(String user_id);
    public List<JobReq> selectJobInfo(int job_posting_no);
    public Integer selectApplyCheck(int cv_no);

    public List<ChartData> selectCountReq(int job_posting_no);
    public List<ChartData> selectCountReqUser(int cv_no);

    // INSERT MAPPER
    public int insertCV(CV cv);
    public int insertActivity(Activity activity);
    public int insertAdvantage(Advantage advantage);
    public int insertCareer(Career career);
    public int insertCertification(Certification certification);
    public int insertCVFile(CVFile cvFile);
    public int insertEducation(Education education);
    public int insertLanguage(Language language);
    public int insertSkill(Skill skill);
    public int insertApply(CV cv, int apply_no);

    // UPDATE MAPPER
    public int updateCV(CV cv);
    public int updateActivity(Activity activity);
    public int updateAdvantage(Advantage advantage);
    public int updateCareer(Career career);
    public int updateCertification(Certification certification);
    public int updateCVFile(CVFile cvFile);
    public int updateEducation(Education education);
    public int updateLanguage(Language language);
    public int updateSkill(Skill skill);

    //    DELETE MAPPER
    public int deleteActivity(int activity_no);
    public int deleteAdvantage(int advantage_no);
    public int deleteCareer(int career_no);
    public int deleteCertification(int cert_no);
    public int deleteEducation(int edu_no);
    public int deleteLanguage(int lang_no);
    public int deleteSkill(int skill_no);
    public int deleteCVFile(int cv_file_no);

}
