package com.nt.service_BASF;

import com.nt.dao_BASF.MapBox_MapLevel;
import com.nt.utils.dao.TokenModel;

import java.util.List;

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
    //查询树结构
    List<MapBox_MapLevel> getall() throws  Exception;

    List<MapBox_MapLevel> list() throws Exception;

    ;

    void add(MapBox_MapLevel info, TokenModel tokenModel) throws Exception;

    ;

    void edit(MapBox_MapLevel info, TokenModel tokenModel) throws Exception;

    ;

    void delete(MapBox_MapLevel mapBox_mapLevel, TokenModel tokenModel) throws Exception;

    ;

    //通过sql获取子节点
    List<String> getChildrensStr(String mapid) throws Exception;

    //根据传来的mapid更改remark
    void remarkSet(List<String> mapidList,boolean zeroOrOne,TokenModel tokenModel) throws Exception;

    void remarkSet(String mapid,boolean zeroOrOne,TokenModel tokenModel) throws Exception;
}
