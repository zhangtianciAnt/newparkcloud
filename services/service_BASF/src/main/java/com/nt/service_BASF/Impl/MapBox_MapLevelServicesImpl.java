package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.MapBox_MapLevel;
import com.nt.dao_BASF.QuestionManage;
import com.nt.service_BASF.MapBox_MapLevelServices;
import com.nt.service_BASF.mapper.MapBox_MapLevelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: MapBox_MapLevelServicesImpl
 * @Author: WXZ
 * @Description: MapBox_MapLevelServicesImpl
 * @Date: 2019/12/17 10:51
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MapBox_MapLevelServicesImpl implements MapBox_MapLevelServices {

    @Autowired
    private MapBox_MapLevelMapper mapBox_mapLevelMapper;

    /**
     * @param mapid
     * @Method one
     * @Author Wxz
     * @Version 1.0
     * @Description 根据id获取详情
     * @Return com.nt.dao_BASF.QuestionManage
     * @Date 2019/11/20 18:49
     */
    @Override
    public MapBox_MapLevel one(String mapid) throws Exception {
        return mapBox_mapLevelMapper.selectByPrimaryKey(mapid);
    }
}
