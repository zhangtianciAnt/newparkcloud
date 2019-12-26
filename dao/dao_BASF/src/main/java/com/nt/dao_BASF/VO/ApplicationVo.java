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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "application")
public class ApplicationVo extends BaseModel {
    @Id
    private String applicationid;

    /**
     * 申请人
     */
    private String customername;

    /**
     * 审查状态
     */
    private String applicationstatus;

    /**
     * 工作内容
     */
    private String workcontent;

    /**
     * 申请的设备名
     */
    private String devicename;

    /**
     * 所在区域
     */
    private String region;

    /**
     * 所在装置
     */
    private String device;

    /**
     * 所在楼层
     */
    private String floor;

    /**
     * 设备id
     */
    private String deviceinformationid;

    /**
     * 设备状态
     */
    private String devicestatus;


}