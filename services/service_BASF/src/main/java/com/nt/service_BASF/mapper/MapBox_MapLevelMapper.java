package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.MapBox_MapLevel;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: MapBox_MapLevelMapper
 * @Author: WXZ
 * @Description: MapBox_MapLevelMapper
 * @Date: 2019/12/17 10:53
 * @Version: 1.0
 */
public interface MapBox_MapLevelMapper extends MyMapper<MapBox_MapLevel> {
    List<MapBox_MapLevel> selectChildrensStr(@Param("mapid") String mapid) throws Exception;

    List<MapBox_MapLevel> getAll() throws Exception;
}
