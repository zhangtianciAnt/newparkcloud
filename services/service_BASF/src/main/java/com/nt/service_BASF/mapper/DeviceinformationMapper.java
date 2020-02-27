package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Deviceinformation;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.Array;
import java.util.List;

public interface DeviceinformationMapper extends MyMapper<Deviceinformation> {
    List<Deviceinformation> selectDeviceList(
            @Param("mapid") String mapid, @Param("devicetype") String[] devicetype,
            @Param("devicename") String devicename, @Param("pageindex") Integer pageindex,
            @Param("pagesize") Integer pagesize);
}