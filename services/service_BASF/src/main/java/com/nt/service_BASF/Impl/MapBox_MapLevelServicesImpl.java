package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.MapBox_MapLevel;
import com.nt.service_BASF.MapBox_MapLevelServices;
import com.nt.service_BASF.mapper.MapBox_MapLevelMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    //查询树结构
    @Override
    public List<MapBox_MapLevel> getall() throws Exception {
        MapBox_MapLevel mapBox_mapLevel = new MapBox_MapLevel();

//        List<MapBox_MapLevel> moduleList  =  mapBox_mapLevelMapper.select(mapBox_mapLevel);
        List<MapBox_MapLevel> moduleList = mapBox_mapLevelMapper.getAll();
        // 生成树
        List<MapBox_MapLevel> result = getChildren(moduleList);
        return result;
    }

    @Override
    public List<MapBox_MapLevel> list() throws Exception {
        MapBox_MapLevel mapBox_mapLevel = new MapBox_MapLevel();
        return mapBox_mapLevelMapper.select(mapBox_mapLevel);
    }
    @Override
    public void add(MapBox_MapLevel info, TokenModel tokenModel) throws Exception {
        info.setId((UUID.randomUUID().toString()));
        info.preInsert(tokenModel);
        mapBox_mapLevelMapper.insert(info);
    }
    @Override
    public void edit(MapBox_MapLevel info, TokenModel tokenModel) throws Exception {
        info.preUpdate(tokenModel);
        mapBox_mapLevelMapper.updateByPrimaryKeySelective(info);
    }
    @Override
    public void delete(MapBox_MapLevel mapBox_mapLevel, TokenModel tokenModel) throws Exception {
//        MapBox_MapLevel mapBox_mapLevel = new MapBox_MapLevel();
//        mapBox_mapLevel.setId(id);
        mapBox_mapLevel.preUpdate(tokenModel);
        mapBox_mapLevelMapper.updateByPrimaryKeySelective(mapBox_mapLevel);
    }



    /**
     * 获取跟节点
     * @param list
     * @return
     */
    private List<MapBox_MapLevel> getChildren(List<MapBox_MapLevel> list) throws Exception {
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
    private MapBox_MapLevel getChildrens(MapBox_MapLevel module, List<MapBox_MapLevel> list) throws Exception {
        List<MapBox_MapLevel> childNodes = new ArrayList<>();
        for (MapBox_MapLevel node : list) {
            if (node.getParentid().equals(module.getId())) {
                childNodes.add(getChildrens(node, list));
            }
        }
        module.setChildren(childNodes);
        return module;
    }

    //通过sql获取子节点
    @Override
    public List<String> getChildrensStr(String mapid) throws Exception {
        if (StringUtils.isNotEmpty(mapid)) {
            List<MapBox_MapLevel> mapBox_mapLevels = mapBox_mapLevelMapper.selectChildrensStr("%" + mapid + "%");
            List<String> childrensStr = new ArrayList<>();
            for (MapBox_MapLevel mapBox_mapLevel : mapBox_mapLevels) {
                childrensStr.add(mapBox_mapLevel.getId());
            }
            childrensStr.add(mapid);
            return childrensStr;
        } else {
            return null;
        }
    }



}
