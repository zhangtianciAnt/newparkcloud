package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Pimsdata;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PimsdataMapper extends MyMapper<Pimsdata> {
    List<Pimsdata> getAllPimsInfo(@Param("type") String type) throws Exception;
}
