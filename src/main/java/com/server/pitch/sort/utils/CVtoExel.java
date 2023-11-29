package com.server.pitch.sort.utils;

import com.server.pitch.cv.domain.*;
import com.server.pitch.sort.domain.ApplicantDetailResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;


@Log4j2
public class CVtoExel {

    public static byte[] copyExcelTemplate(ApplicantDetailResponse applicant) throws IOException {
        String path = "C:" + File.separator +  "pitch_resorces" + File.separator;

        String originalFilePath = path + "입사지원서양식.xlsx";
        String copiedFilePath = path + "입사지원서("+ applicant.getCv().getUser_nm() +").xlsx";

        FileInputStream originalFile = new FileInputStream(originalFilePath);
        Workbook originalWorkbook = new XSSFWorkbook(originalFile);
        Workbook copiedWorkbook = new XSSFWorkbook();
        CellStyleCopier cellStyleCopier = new CellStyleCopier(copiedWorkbook);

        for (int i = 0; i < originalWorkbook.getNumberOfSheets(); i++) {
            Sheet originalSheet = originalWorkbook.getSheetAt(i);
            Sheet copiedSheet = copiedWorkbook.createSheet(originalSheet.getSheetName());

            for (int j = 0; j < originalSheet.getNumMergedRegions(); j++) {
                CellRangeAddress mergedRegion = originalSheet.getMergedRegion(j);
                copiedSheet.addMergedRegion(mergedRegion);
            }

            for (int j = 0; j < originalSheet.getPhysicalNumberOfRows(); j++) {
                Row originalRow = originalSheet.getRow(j);
                Row copiedRow = copiedSheet.createRow(j);
                if (originalRow != null) {
                    for (int k = 0; k < originalRow.getPhysicalNumberOfCells(); k++) {
                        Cell originalCell = originalRow.getCell(k);
                        Cell copiedCell = copiedRow.createCell(k);
                        if (originalCell != null) {
                            copiedCell.setCellValue(originalCell.getStringCellValue());
                        }
                    }
                }
            }
        }

        Sheet firstSheet = copiedWorkbook.getSheetAt(0);
        byte[] imageBytes = Base64.getDecoder().decode(applicant.getPicture());

        int pictureIdx = copiedWorkbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
        CreationHelper helper = copiedWorkbook.getCreationHelper();
        Drawing<?> drawing = firstSheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(0);
        anchor.setRow1(3);

        anchor.setCol2(2);
        anchor.setRow2(9);

        Picture picture = drawing.createPicture(anchor, pictureIdx);

        Row nameRow = firstSheet.getRow(3);
        Cell nameCell = nameRow.createCell(4);
        nameCell.setCellValue(applicant.getCv().getUser_nm());

        nameRow = firstSheet.getRow(4);
        nameCell = nameRow.createCell(4);
        nameCell.setCellValue(applicant.getCv().getUser_birth());

        nameRow = firstSheet.getRow(5);
        nameCell = nameRow.createCell(4);
        nameCell.setCellValue(applicant.getCv().getUser_phone());

        nameRow = firstSheet.getRow(6);
        nameCell = nameRow.createCell(4);
        nameCell.setCellValue(applicant.getCv().getAddress());

        nameRow = firstSheet.getRow(4);
        nameCell = nameRow.createCell(7);
        nameCell.setCellValue("개발");

        nameRow = firstSheet.getRow(5);
        nameCell = nameRow.createCell(7);
        nameCell.setCellValue(applicant.getCv().getUser_email());

        for(Advantage advantage : applicant.getCv().getAdvantages()) {
            if(advantage.getAdvantage_type() == null) break;
            if(advantage.getAdvantage_type().equals("보훈 대상")) {
                nameRow = firstSheet.getRow(7);
                nameCell = nameRow.createCell(7);
                nameCell.setCellValue("해당");
            } else if(advantage.getAdvantage_type().equals("취업보호 대상")) {
                nameRow = firstSheet.getRow(8);
                nameCell = nameRow.createCell(7);
                nameCell.setCellValue("해당");
            } else if(advantage.getAdvantage_type().equals("장애 대상")) {
                nameRow = firstSheet.getRow(8);
                nameCell = nameRow.createCell(4);
                nameCell.setCellValue("해당");
            } else if(advantage.getAdvantage_type().equals("병역")) {
                nameRow = firstSheet.getRow(7);
                nameCell = nameRow.createCell(4);
                nameCell.setCellValue(advantage.getAdvantage_detail());
            }

        }
        int row = 10;

        for(Education edu : applicant.getCv().getEducations()) {
            if(edu.getEdu_type()== null) break;
            nameRow = firstSheet.getRow(row);
            nameCell = nameRow.createCell(1);
            nameCell.setCellValue(edu.getEdu_type());

            nameCell = nameRow.createCell(3);
            nameCell.setCellValue(convertToExcelDate(edu.getEnter_date()));

            nameCell = nameRow.createCell(4);
            nameCell.setCellValue(convertToExcelDate(edu.getGraduate_date()));

            nameCell = nameRow.createCell(5);
            nameCell.setCellValue(edu.getMajor());

            nameCell = nameRow.createCell(7);
            nameCell.setCellValue(edu.getGraduate_type());

            nameCell = nameRow.createCell(8);
            nameCell.setCellValue(edu.getScore() + "/" + edu.getTotal_score());
            row++;
        }

        row = 15;

        for(Career career : applicant.getCv().getCareers()) {
            if(career.getCompany_name() == null) break;
            nameRow = firstSheet.getRow(row);
            nameCell = nameRow.createCell(1);
            nameCell.setCellValue(career.getCompany_name());


            nameCell = nameRow.createCell(3);
            if(career.getQuit_date() == null) {
                nameCell.setCellValue(convertToExcelDate(career.getJoin_date()) + " ~ 재직중");
            } else {
                nameCell.setCellValue(
                        convertToExcelDate(career.getJoin_date()) + " ~ " + convertToExcelDate(career.getQuit_date()));
            }

            nameCell = nameRow.createCell(6);
            nameCell.setCellValue(career.getPosition());

            nameCell = nameRow.createCell(7);
            nameCell.setCellValue(career.getJob());

            nameCell = nameRow.createCell(8);
            nameCell.setCellValue(career.getSalary());
            row++;
        }

        row = 23;
        for(Certification cert : applicant.getCv().getCertifications()) {
            if(cert.getCert_name() == null) break;
            nameRow = firstSheet.getRow(row);
            nameCell = nameRow.createCell(1);
            nameCell.setCellValue(cert.getCert_name());

            nameCell = nameRow.createCell(3);
            nameCell.setCellValue(convertToExcelDate(cert.getAcquisition_date()));

            nameCell = nameRow.createCell(4);
            nameCell.setCellValue(cert.getPublisher());

            row++;
        }

        row = 23;
        for(Language lang : applicant.getCv().getLanguages()) {
            if(lang.getLanguage_name() ==null) break;
            nameRow = firstSheet.getRow(row);
            nameCell = nameRow.createCell(6);
            nameCell.setCellValue(lang.getLanguage_name());

            nameCell = nameRow.createCell(7);
            nameCell.setCellValue(lang.getExam_type());

            nameCell = nameRow.createCell(8);
            nameCell.setCellValue(lang.getLanguage_score());

            row++;
        }

        row = 29;
        for(Activity activity : applicant.getCv().getActivities()) {
            if(activity.getOrganization() == null) break;
            nameRow = firstSheet.getRow(row);
            nameCell = nameRow.createCell(1);
            nameCell.setCellValue(activity.getActivity_type());

            nameCell = nameRow.createCell(2);
            nameCell.setCellValue(activity.getOrganization());

            nameCell = nameRow.createCell(4);
            nameCell.setCellValue(
                    convertToExcelDate(activity.getStart_date()) + " ~ " + convertToExcelDate(activity.getEnd_date()));

            nameCell = nameRow.createCell(6);
            nameCell.setCellValue(activity.getActivity_detail());

            row++;
        }

        for (int i = 0; i < originalWorkbook.getNumberOfSheets(); i++) {
            Sheet originalSheet = originalWorkbook.getSheetAt(i);
            Sheet copiedSheet = copiedWorkbook.getSheetAt(i);

            for (int j = 0; j < originalSheet.getPhysicalNumberOfRows(); j++) {
                Row originalRow = originalSheet.getRow(j);
                Row copiedRow = copiedSheet.getRow(j);
                for (int k = 0; k < originalRow.getPhysicalNumberOfCells(); k++) {
                    Cell originalCell = originalRow.getCell(k);
                    Cell copiedCell = copiedRow.getCell(k);

                    CellStyle newCellStyle = cellStyleCopier.copyCellStyle(originalCell.getCellStyle());
                    copiedCell.setCellStyle(newCellStyle);

                    copiedSheet.setColumnWidth(k, originalSheet.getColumnWidth(k));
                    copiedRow.setHeight(originalRow.getHeight());
                }
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(copiedFilePath)) {
            copiedWorkbook.write(fileOut);
        }

        originalWorkbook.close();
        copiedWorkbook.close();
        originalFile.close();

        return readExcelFile(copiedFilePath);
    }

    static class CellStyleCopier {
        private final Workbook workbook;

        public CellStyleCopier(Workbook workbook) {
            this.workbook = workbook;
        }

        public CellStyle copyCellStyle(CellStyle src) {
            CellStyle dest = workbook.createCellStyle();
            dest.cloneStyleFrom(src);
            return dest;
        }
    }

    public static String convertToExcelDate(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        } else {
            return "";
        }
    }

    public static byte[] readExcelFile(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0]; // 오류 발생 시 빈 배열 반환
        }
    }

}
