package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Application;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "application")
public class ApplicationVo extends BaseModel {

    Application application;

    /**
     * 申请类别（0为消防水，1为道路占用）
     */
    private String type;
    /**
     * 流程ID
     */
    private String workflowid;

    /**
     * GIS坐标
     */
    private String gis;

    /**
     * 地图信息主键
     */
    private String mapid;

    /**
     * 流程名称
     */
    private String workflowname;

}