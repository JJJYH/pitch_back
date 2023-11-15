package com.server.pitch.sort.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDownloadRequest {
    private String title;
    private List<Integer> applyNo;
    private List<String> type;
}
