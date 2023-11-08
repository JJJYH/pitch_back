package com.server.pitch.sort.controller;

import com.server.pitch.sort.domain.*;
import com.server.pitch.sort.service.SortService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.util.*;


/**
 * 선택된 공고의 지원자들을 선별/관리하는 로직을 처리하는 컨트롤러입니다.
 *
 * @author soyoung
 */
@Log4j2
@RestController
@RequestMapping("/admin")
public class SortController {
    @Autowired
    SortService service;


    /**
     * 지원자 목록을 조회하는 api
     *
     * @param job_posting_no 관리할 공고 번호
     * @return List<ApplicantResponse> 지원자 목록
     */
    @GetMapping("/{job_posting_no}/sort")
    public List<ApplicantResponse> sortAll(@PathVariable int job_posting_no, @RequestParam String type) {
        log.info("==================================applicant list controller=====================================");

        return service.findAll(new ApplicantRequest(job_posting_no, "", type));
    }

    /**
     * 공고 정보를 조회하는 api
     *
     * @param job_posting_no 관리할 공고 번호
     * @return PostingInfoResponse 공고 정보
     */
    @GetMapping("/{job_posting_no}/info")
    public PostingInfoResponse postingInfoOne(@PathVariable int job_posting_no) {
        log.info("==================================posting info controller=====================================");

        return service.findInfoByPostingNo(job_posting_no);
    }


    /**
     * 지원자 합격/불합격 대기 상태를 처리하는 api
     *
     * @param data 타입(합격대기/불합격대기)과 지원번호 목록
     */
    @PutMapping("/type")
    public void handleStatusType(@RequestBody List<Map<String, Object>> data) {
        log.info("==================================acceptance controller=====================================");

        service.statusTypeUpdate(data);
    }

    /**
     * 지원자 합격/불합격 처리를 하는 api
     *
     * @param job_posting_no,data 타입(합격/불합격)과 발표 양식, 지원자 목록
     */
    @PutMapping("/{job_posting_no}/acceptance")
    public void handleAcceptance(@PathVariable int job_posting_no, @RequestBody Map<String, Object> data) {
        log.info("==================================acceptance controller=====================================");

        service.statusUpdate(data);
    }

    /**
     * 지원자 상세 정보를 조회하는 api
     *
     * @param apply_no 지원자 번호
     * @return ApplicantDetailResonse 지원자 상세
     */
    @GetMapping("/{apply_no}/detail")
    public ApplicantDetailResponse cvOne(@PathVariable int apply_no) {
        log.info("==================================applicant detail controller=====================================");

        return service.findOne(apply_no);
    }

    /**
     * 지원자 면접 점수를 평가하는 api
     *
     * @param apply_no 평가할 지원자 번호
     */
    @PostMapping("/{apply_no}/evaluation")
    public void evaluate(@PathVariable int apply_no, @RequestBody CandidateEval eval) {
        log.info("==================================applicant evaluate controller=====================================");

        service.createEval(eval);
    }

    /**
     * 자기소개서 워드클라우드 api
     *
     * @return List<Map<String, Object>> 명사, 사용횟수
     */
    @GetMapping("/test-word")
    public List<Map<String, Object>> testController() throws Exception {
        String filePath = "C://더존ICT그룹 입사지원서-(성명)_v7.3.xlsx";
        Workbook workbook = WorkbookFactory.create(new File(filePath));

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
        log.info(Arrays.deepToString(text));
        List<Map<String, Object>> rMap = service.doWordAnalysis(Arrays.deepToString(text));

        if (rMap == null) {
            rMap = new ArrayList<>();
        }

        return rMap;
    }

    /**
     * 선별된 지원자 리스트 api
     *
     * @param job_posting_no 공고번호
     * @return List<ApplicantDetailResponse>  선별된 지원자 목록
     */
    @PostMapping("/{job_posting_no}/filter")
    public List<ApplicantDetailResponse> filteredApplicantList(@PathVariable int job_posting_no, @RequestBody FilterRequest filter) {
        log.info("==================================filtered applicant list controller=====================================");
    
        return service.findAllFilteredApplicant(job_posting_no, filter);
    }
}

