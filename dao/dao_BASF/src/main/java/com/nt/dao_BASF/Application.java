package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "application")
public class Application extends BaseModel {
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
}