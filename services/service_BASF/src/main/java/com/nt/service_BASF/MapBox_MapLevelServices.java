package com.nt.service_BASF;

import com.nt.dao_BASF.MapBox_MapLevel;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: MapBox_MapLevelServices
 * @Author: WXZ
 * @Description: MapBox_MapLevelServices
 * @Date: 2019/12/17 10:46
 * @Version: 1.0
 */
public interface MapBox_MapLevelServices {

    //根据id获取详情
    MapBox_MapLevel one(String mapid) throws Exception;
}
