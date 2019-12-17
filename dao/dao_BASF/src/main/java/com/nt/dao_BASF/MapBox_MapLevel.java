package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: mapbox_maplevel
 * @Author: WXZ
 * @Description: mapbox_maplevel
 * @Date: 2019/12/17 10:39
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mapbox_maplevel")
public class MapBox_MapLevel extends BaseModel {

    @Id
    private String id;

    /**
     *级别 1厂区 2区域 3建筑和装置 4楼层
     */
    private String level;

    /**
     *
     */
    private String name;

    /**
     *简称
     */
    private String shortname;

    /**
     *
     */
    private String parentid;

    /**
     *备注
     */
    private String remark;

    /**
     *存放中心点坐标
     */
    private String center;

    /**
     *中心点缩放级别
     */
    private String zoom;

    /**
     *存放json
     */
    private String geojson;

}
