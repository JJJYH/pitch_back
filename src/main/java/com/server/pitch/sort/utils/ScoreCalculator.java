package com.server.pitch.sort.utils;

import com.server.pitch.cv.domain.*;
import com.server.pitch.sort.domain.ApplicantDetailResponse;
import com.server.pitch.sort.domain.FilterRequest;
import com.server.pitch.sort.domain.Score;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class ScoreCalculator {
    public static void calculateScore(FilterRequest filter, ApplicantDetailResponse applicant) {
        double totalScore = 0;

        if (filter.getAdvantageScore() != 0) {
            double advantageScore = calculateAdvantageScore(applicant.getCv().getAdvantages());
            double aScore = filter.getAdvantageScore() * advantageScore;
            totalScore += aScore;
        }

        if (filter.getCareerScore() != 0) {
            double experienceScore = calculateCareerScore(filter.getJob_type(), filter.getJob_year(), applicant.getCv().getCareers());
            double cScore = filter.getCareerScore() * experienceScore;
            totalScore += cScore;
        }

        if (filter.getCertificationScore() != 0) {
            double certificationScore = calculateCertificationScore(filter.getJob_group(), applicant.getCv().getCertifications());
            double cScore = filter.getCertificationScore() * certificationScore;
            totalScore += cScore;
        }

        if (filter.getLanguageScore() != 0) {
            double languageScore = calculateLanguageScore(applicant.getCv().getLanguages());
            double lScore = filter.getLanguageScore() * languageScore;
            totalScore += lScore;
        }

        if (filter.getEducationScore() != 0) {
            double educationScore = calculateEducationScore(applicant.getCv().getEducations(), filter);
            double eScore = filter.getEducationScore() * educationScore;
            totalScore += eScore;
        }


        applicant.setScore((int) totalScore);
    }

    public static void calculateScore(FilterRequest filter, Score score, CV cv) {
        double totalScore = 0;

        if (filter.getAdvantageScore() != 0) {
            double advantageScore = calculateAdvantageScore(cv.getAdvantages());
            double aScore = filter.getAdvantageScore() * advantageScore;
            totalScore += aScore;
            score.setAdvantage_score((int) aScore);
        }

        if (filter.getCareerScore() != 0) {
            double experienceScore = calculateCareerScore(filter.getJob_type(), filter.getJob_year(), cv.getCareers());
            double cScore = filter.getCareerScore() * experienceScore;
            totalScore += cScore;
            score.setCareer_score((int) cScore);
        }

        if (filter.getCertificationScore() != 0) {
            double certificationScore = calculateCertificationScore(filter.getJob_group(), cv.getCertifications());
            double cScore = filter.getCertificationScore() * certificationScore;
            totalScore += cScore;
            score.setCertification_score((int) cScore);
        }

        if (filter.getLanguageScore() != 0) {
            double languageScore = calculateLanguageScore(cv.getLanguages());
            double lScore = filter.getLanguageScore() * languageScore;
            totalScore += lScore;
            score.setLanguage_score((int) lScore);
        }

        if (filter.getEducationScore() != 0) {
            double educationScore = calculateEducationScore(cv.getEducations(), filter);
            double eScore = filter.getEducationScore() * educationScore;
            totalScore += eScore;

            score.setEducation_score((int) eScore);
        }


        score.setScore((int) totalScore);
    }

    private static double calculateCareerScore(String job_type, String job_year, List<Career> careers) {
        double experienceScore = 0;
        double totalExperienceYears = 0.0;
        List<Career> sortedCareers = new ArrayList<>(careers);

        for(Career career : careers) {
            if(career.getCompany_name() == null) return 0.5;
            Date joinDate = career.getJoin_date();
            Date quitDate = career.getQuit_date();

            if (joinDate != null && quitDate != null) {
                double experienceYears = calculateYearsBetweenDates(joinDate, quitDate);
                totalExperienceYears += experienceYears;
            }
        }
        sortedCareers.sort(Comparator.comparing(Career::getQuit_date).reversed());
        int yearsSinceQuit = calculateYearsSinceQuit(sortedCareers.get(0).getQuit_date());

        if(yearsSinceQuit >= 5) {
            experienceScore = 0.1;
        } else {
            experienceScore = 1;
        }

        if (job_type.equals("신입")) {
            if(totalExperienceYears >= 2) {
                experienceScore *= 1;
            }
        } else if (job_type.equals("경력")) {
            int requiredYears = Integer.parseInt(job_year);

            if (totalExperienceYears >= requiredYears) {
                experienceScore *= 1.0;
            } else if (yearsSinceQuit == requiredYears - 1) {
                experienceScore *= 0.8;
            }
        }

        return experienceScore;
    }

    private static double calculateCertificationScore(String job_group, List<Certification> certs) {
        boolean hasCert = false;
        boolean hasSecondCert = false;
        double certificationScore = 0;

        for(Certification cert : certs) {
            if(cert.getCert_name() == null) break;
            if(hasCert) {
                hasSecondCert = checkCert(cert.getCert_name(), job_group);
            } else {
                hasCert = checkCert(cert.getCert_name(), job_group);
            }
        }

        if(hasCert && hasSecondCert) {
            certificationScore = 1;
        } else if(hasCert) {
            certificationScore = 0.5;
        }

        return certificationScore;
    }

    private static double calculateLanguageScore(List<Language> languages) {
        boolean hasPassedEnglish = false;
        boolean hasPassedSecondLanguage = false;
        double languageScore = 0.0;

        for (Language language : languages) {
            if(language.getLanguage_name() == null) break;
            String languageName = language.getLanguage_name().equals("영어") ? "영어" : "제2외국어";
            int examScore = (int) language.getLanguage_score();
            String examType = language.getExam_type();
            double score = 0;

            switch (languageName) {
                case "영어":
                    if (examType.equals("TOEIC")) {
                        score = calculateTOEICScore(examScore);
                        if(score > 0) {
                            hasPassedEnglish = true;
                        }
                    } else if (examType.equals("TOEFL")) {
                        score = calculateTOEFLScore(examScore);
                        if(score > 0) {
                            hasPassedEnglish = true;
                        }
                    }
                    break;
                case "제2외국어":
                    if (examType.equals("JPT")) {
                        score = calculateJPTScore(examScore);
                        if(score > 0) {
                            hasPassedSecondLanguage = true;
                        }
                    } else if (examType.equals("HSK")) {
                        score = calculateHSKScore(examScore);
                        if(score > 0) {
                            hasPassedSecondLanguage = true;
                        }
                    }
                    break;
            }
            languageScore += score;
        }

        if (hasPassedEnglish && hasPassedSecondLanguage) {
            languageScore *= 1.0;
        } else if (hasPassedEnglish) {
            languageScore *= 0.5;
        } else if (hasPassedSecondLanguage) {
            languageScore *= 0.3;
        }

        return languageScore;
    }

    private static double calculateEducationScore(List<Education> educations, FilterRequest filter) {
        double educationScore = 0;
        List<Education> sortedEducations = new ArrayList<>(educations);
        sortedEducations.sort(Comparator.comparing(Education::getGraduate_date).reversed());

        String highestEdu = sortedEducations.get(0).getEdu_type();
        if(highestEdu == null) return 0;

        String regexForCollege = ".*대학교.*";
        String regexForTechCollege = ".*전문대.*";
        String regexForHighSchool = ".*고등학교.*";

        Pattern patternForCollege = Pattern.compile(regexForCollege);
        Pattern patternForTechCollege = Pattern.compile(regexForTechCollege);
        Pattern patternForHighSchool = Pattern.compile(regexForHighSchool);

        double score = 0;
        double collageScore = 0;

        if (patternForCollege.matcher(highestEdu).matches()) {
            score = 1;
            collageScore =checkCollage(highestEdu);
            educationScore += collageScore * 0.3;
        } else if (patternForTechCollege.matcher(highestEdu).matches()) {
            score = 0.8;
        } else if (patternForHighSchool.matcher(highestEdu).matches()) {
            score = 0.3;
        }

        educationScore += score * 0.3;


        double convertedScore = (sortedEducations.get(0).getScore() / sortedEducations.get(0).getTotal_score());
        educationScore += convertedScore * 0.2;

        boolean isMajored = checkMajor(sortedEducations.get(0).getMajor(), filter.getJob_group());

        if(isMajored) {
            educationScore += 0.2;
        }

        return educationScore;
    }

    private static double calculateAdvantageScore(List<Advantage> advantages) {
        double score = 0;
        for (Advantage advantage : advantages) {
            if(advantage.getAdvantage_type() == null) break;
            if (advantage.getAdvantage_type().equals("병역") && advantage.getAdvantage_detail().equals("군필")) {
                score += 0.2;
            } else if (advantage.getAdvantage_type().equals("보훈 대상")) {
                score += 0.2;
            } else if (advantage.getAdvantage_type().equals("취업보호 대상")) {
                score += 0.2;
            } else if (advantage.getAdvantage_type().equals("장애")) {
                score += 0.2;
            } else if (advantage.getAdvantage_type().equals("고용지원금 대상")) {
                score += 0.2;
            }
        }

        return score;
    }
    private static double calculateTOEICScore(int examScore) {
        if (examScore >= 900) return 1.0;
        else if (examScore >= 800) return 0.8;
        else if (examScore >= 750) return 0.5;
        else if (examScore >= 700) return 0.3;
        else return 0;
    }
    private static double calculateTOEFLScore(int examScore) {
        if (examScore >= 105) return 1.0;
        else if (examScore >= 95) return 0.8;
        else if (examScore >= 88) return 0.5;
        else if (examScore >= 80) return 0.3;
        else return 0;
    }

    private static double calculateJPTScore(int examScore) {
        if (examScore >= 880) return 1;
        else if (examScore >= 750) return 0.8;
        else if (examScore >= 700) return 0.5;
        else if (examScore >= 650) return 0.3;
        else return 0;
    }

    private static double calculateHSKScore(int examScore) {
        if (examScore >= 6) return 1.0;
        else if (examScore >= 5) return 0.8;
        else if (examScore >= 4) return 0.5;
        else if (examScore >= 3) return 0.3;
        else return 0;
    }
    private static int calculateYearsSinceQuit(Date quitDate) {
        LocalDate quitLocalDate = quitDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        return (int) ChronoUnit.YEARS.between(quitLocalDate, currentDate);
    }

    private static double calculateYearsBetweenDates(Date startDate, Date endDate) {
        long millisecondsPerYear = 1000L * 60L * 60L * 24L * 365L;
        long timeDifference = endDate.getTime() - startDate.getTime();
        return (double) timeDifference / millisecondsPerYear;
    }

    private static boolean checkMajor(String major, String job_group) {
        boolean isMajored = false;
        if(job_group.equals("개발")) {
            String regex = "정보|공학|컴퓨터";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(major);

            isMajored = matcher.find();
        } else if (job_group.equals("경영/회계")) {
            String regex = "경영|회계|금융|경제|인사";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(major);

            isMajored = matcher.find();
        } else if (job_group.equals("영업/마케팅")) {
            String regex = "광고|마케팅|영업";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(major);

            isMajored = matcher.find();
        }
        return isMajored;
    }

    private static double checkCollage(String collage) {
        String regex = "서울대|카이스트|포항공대|의대|치대|한의대|연세대|고려대|성균관대|한양대|서강대|중앙대|경희대|외대|서울시립대|이화여대";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(collage);

        if(matcher.find()) {
            return 1;
        } else {
            return 0;
        }
    }

    private static boolean checkCert(String cert, String job_group) {
        boolean isQualifiedCert = false;
        if(job_group.equals("개발")) {
            String regex = "정보처리기사|리눅스마스터|sqld";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(cert);

            isQualifiedCert = matcher.find();
        } else if (job_group.equals("경영/회계")) {
            String regex = "전산회계|전산세무|ERP|컴퓨터활용";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(cert);

            isQualifiedCert = matcher.find();
        } else if (job_group.equals("영업/마케팅")) {
            String regex = "컴퓨터활용|포토샵|구글|경영지도사";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(cert);

            isQualifiedCert = matcher.find();
        }

        return isQualifiedCert;
    }
}
