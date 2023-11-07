package com.server.pitch.sort.utils;

import com.server.pitch.cv.domain.*;
import com.server.pitch.sort.domain.ApplicantDetailResponse;
import com.server.pitch.sort.domain.FilterRequest;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Log4j2
public class ScoreCalculator {
    public static void calculateScore(FilterRequest filter, ApplicantDetailResponse applicant) {

        double totalScore = 0;

        if (filter.getAdvantageScore() != 0) {
            double advantageScore = calculateAdvantageScore(applicant.getCv().getAdvantages());
            totalScore += filter.getAdvantageScore() * advantageScore;
        }

        if (filter.getCareerScore() != 0) {
            double experienceScore = calculateCareerScore(filter.getJob_type(), filter.getJob_year(), applicant.getCv().getCareers());
            totalScore += filter.getCareerScore() * experienceScore;
        }

        if (filter.getCertificationScore() != 0) {
            double certificationScore = calculateCertificationScore(applicant.getCv().getCertifications());
            totalScore += filter.getCertificationScore() * certificationScore;
        }

        if (filter.getLanguageScore() != 0) {
            double languageScore = calculateLanguageScore(applicant.getCv().getLanguages());
            totalScore += filter.getLanguageScore() * languageScore;
        }

        if (filter.getEducationScore() != 0) {
            double educationScore = calculateEducationScore(applicant.getCv().getEducations());
            totalScore += filter.getEducationScore() * educationScore;
        }


        applicant.setScore((int) totalScore);
    }

    private static double calculateCareerScore(String job_type, String job_year, List<Career> careers) {
        double experienceScore = 0;
        double totalExperienceYears = 0.0;
        List<Career> sortedCareers = new ArrayList<>(careers);

        for(Career career : careers) {
            Date joinDate = career.getJoin_date();
            Date quitDate = career.getQuit_date();

            if (joinDate != null && quitDate != null) {
                double experienceYears = calculateYearsBetweenDates(joinDate, quitDate);
                totalExperienceYears += experienceYears;
            }
            sortedCareers.sort(Comparator.comparing(Career::getQuit_date).reversed());
        }

        int yearsSinceQuit = calculateYearsSinceQuit(sortedCareers.get(0).getQuit_date());

        if(yearsSinceQuit >= 5) {
            experienceScore = 0.1;
        } else {
            experienceScore = 1;
        }

        if (job_type.equals("신입")) {
            if(totalExperienceYears >= 3) {
                experienceScore *= 1;
            } else if(totalExperienceYears >= 2) {
                experienceScore *= 0.5;
            } else if(totalExperienceYears >= 1) {
                experienceScore *= 0.2;
            }
        } else if (job_type.equals("경력")) {
            int requiredYears = Integer.parseInt(job_year);

            if (totalExperienceYears >= requiredYears) {
                experienceScore *= 1.0;
            } else if (yearsSinceQuit == requiredYears - 1) {
                experienceScore *= 0.5;
            } else if (yearsSinceQuit == requiredYears - 2) {
                experienceScore *= 0.2;
            } else {
                experienceScore *= 0;
            }
        }

        log.info("experienceScore" + experienceScore);
        return experienceScore;
    }

    private static double calculateCertificationScore(List<Certification> certs) {
        double certificationScore = 0;



        return certificationScore;
    }

    private static double calculateLanguageScore(List<Language> languages) {
        boolean hasPassedEnglish = false;
        boolean hasPassedSecondLanguage = false;
        double languageScore = 0.0;

        for (Language language : languages) {
            String languageName = language.getLanguage_name().equals("영어") ? "영어" : "제2외국어";
            int examScore = (int) language.getLanguage_score();
            String examType = language.getExam_type();
            int score = 0;

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

        log.info("languageScore : " + languageScore);
        return languageScore;
    }

    private static double calculateEducationScore(List<Education> educations) {
        double educationScore = 0;
        List<Education> sortedEducations = new ArrayList<>(educations);

        return educationScore;
    }

    private static double calculateAdvantageScore(List<Advantage> advantages) {
        double score = 0;
        for (Advantage advantage : advantages) {
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

        log.info("score : " + score);
        return score;
    }
    private static int calculateTOEICScore(int examScore) {
        if (examScore >= 900) return 100;
        else if (examScore >= 800) return 80;
        else if (examScore >= 750) return 50;
        else if (examScore >= 700) return 30;
        else return 0;
    }
    private static int calculateTOEFLScore(int examScore) {
        if (examScore >= 105) return 100;
        else if (examScore >= 95) return 80;
        else if (examScore >= 88) return 50;
        else if (examScore >= 80) return 30;
        else return 0;
    }

    private static int calculateJPTScore(int examScore) {
        if (examScore >= 880) return 100;
        else if (examScore >= 750) return 80;
        else if (examScore >= 700) return 50;
        else if (examScore >= 650) return 30;
        else return 0;
    }

    private static int calculateHSKScore(int examScore) {
        if (examScore >= 6) return 100;
        else if (examScore >= 5) return 80;
        else if (examScore >= 4) return 50;
        else if (examScore >= 3) return 30;
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
}
