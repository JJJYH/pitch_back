package com.server.pitch.cv.mapper;

import com.server.pitch.cv.domain.CV;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CVMapper {
    public CV selectCVList();

}
