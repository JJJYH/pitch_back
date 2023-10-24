package com.server.pitch.cv.service;

import com.server.pitch.cv.domain.CV;
import com.server.pitch.cv.mapper.CVMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CVServiceImpl implements CVService {

    @Autowired
    CVMapper cvMapper;

    @Override
    public CV findAll() {
        return cvMapper.selectCVList();
    }
}
