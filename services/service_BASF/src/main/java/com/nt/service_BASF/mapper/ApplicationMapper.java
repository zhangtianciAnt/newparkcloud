package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.VO.ApplicationVo;
import com.nt.dao_BASF.Application;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "ApplicationMapper")
public interface ApplicationMapper extends MyMapper<Application> {
    List<ApplicationVo> selectApplicationVoList() throws Exception;

    List<Application> selectBarricades() throws Exception;
}