package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.MapBox_MapLevel;
import com.nt.dao_BASF.QuestionManage;
import com.nt.service_BASF.MapBox_MapLevelServices;
import com.nt.service_BASF.mapper.MapBox_MapLevelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<MapBox_MapLevel> getall() throws Exception {
        MapBox_MapLevel mapBox_mapLevel = new MapBox_MapLevel();

        List<MapBox_MapLevel> moduleList  =  mapBox_mapLevelMapper.select(mapBox_mapLevel);
        // 生成树
        List<MapBox_MapLevel> result = getChildren(moduleList);
        return result;
    }

    /**
     * 获取跟节点
     * @param list
     * @return
     */
    private List<MapBox_MapLevel> getChildren(List<MapBox_MapLevel> list) {
        List<MapBox_MapLevel> result = new ArrayList<>();
        for (MapBox_MapLevel adModule : list) {
            // 根节点
            if (adModule.getParentid().equals("0")) {
                result.add(getChildrens(adModule, list));
            }
        }
        return result;
    }

    /**
     * 递归获取子节点
     * @param module
     * @param list
     * @return
     */
    private MapBox_MapLevel getChildrens(MapBox_MapLevel module, List<MapBox_MapLevel> list) {
        List<MapBox_MapLevel> childNodes = new ArrayList<>();
        for (MapBox_MapLevel node : list) {
            if (node.getParentid().equals(module.getId())) {
                childNodes.add(getChildrens(node, list));
            }
        }
        module.setChildren(childNodes);
        return module;
    }



}
