package com.nt.dao_BASF.VO;

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
@Table(name = "alarmreceipt")
public class AlarmreceiptVo extends BaseModel {
    @Id
    private String alarmreceiptid;

    /**
     * 接警单编号
     */
    private String alarmreceiptno;

    /**
     * 接警单类别
     */
    private String alarmreceipttype;

    /**
     * 报警时间
     */
    private Date alarmtime;

    /**
     * 报警级别
     */
    private String alarmlevel;

    /**
     * 报警指标名称
     */
    private String alarmname;

    /**
     * 监测值
     */
    private String detectionvalue;

    /**
     * 详细位置描述
     */
    private String detailedlocation;

    /**
     * 监测设备编号
     */
    private String devicename;

    /**
     * 设备名称
     */
    private String devicerealname;

    /**
     * 责任人
     */
    private String personliable;

    /**
     * 所在区域
     */
    private String region;

    /**
     * 所在防区
     */
    private String defencearea;

    /**
     * 所在装置
     */
    private String device;

    /**
     * 所在楼层
     */
    private String floor;

}