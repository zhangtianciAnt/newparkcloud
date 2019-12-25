package com.nt.dao_BASF;
/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: application
 * @Description: 消防设备申请数据表
 * @Author: LXY
 * @CreateDate: 2019/11/04
 * @Version: 1.0
 */
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
     *申请类别
     */
    private String applicationtype;

    /**
     *具体位置
     */
    private String specificlocation;

    /**
     *申请设备
     */
    private String applicationdevice;

    /**
     *申请时间
     */
    private String applicationtime;

    /**
     *复原/归还时间
     */
    private String returntime;

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