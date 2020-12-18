package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Existtrainarchives;
import com.nt.dao_BASF.VO.ExisttrainarchivesVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExisttrainarchivesMapper extends MyMapper<Existtrainarchives> {

    List<ExisttrainarchivesVo> getAllList() throws Exception;

    ExisttrainarchivesVo getExisttrainarchivesInfoById(@Param("id") String id) throws Exception;

    List<ExisttrainarchivesVo> existtrainarchivesList() throws Exception;

    int deleteAll() throws Exception;
}
