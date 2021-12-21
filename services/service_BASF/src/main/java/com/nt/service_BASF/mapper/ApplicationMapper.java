package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.VO.ApplicationVo;
import com.nt.dao_BASF.Application;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component(value = "ApplicationMapper")
public interface ApplicationMapper extends MyMapper<Application> {
    List<ApplicationVo> selectApplicationVoList() throws Exception;

    List<Application> selectBarricades() throws Exception;

    List<Application> roadClosed() throws Exception;

    List<Application> getAllInfo(@Param("applicationtype") String applicationtype);

    // 系统服务，获取所有待归还数据
    List<Application> getAllReturnBack() throws Exception;

    // 根据设备id模糊查询，获取申请列表
    List<Application> getAllByDeviceInformationId(@Param("deviceinformationid") String deviceinformationid);
}
