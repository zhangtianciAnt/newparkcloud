package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: Duty
 * @Author: 王哲
 * @Description: 值班信息表
 * @Date: 2019/12/25 10:33
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "duty")
public class Duty extends BaseModel {
    /**
     * 值班信息主键
     */
    @Id
    private String dutyid;

    /**
     * 值班时间
     */
    private Date dutytime;

    /**
     * 值班类型（A或B）
     */
    private String dutytype;

    /**
     * 白天值班人
     */
    private String dutydaytime;

    /**
     * 晚上值班人
     */
    private String dutynight;

    /**
     * 其他值班人
     */
    private String dutyother;

    /**
     * 备勤白天
     */
    private String backupdaytime;

    /**
     * 备勤晚上
     */
    private String backupnight;

    /**
     * 备勤其他
     */
    private String backupother;

}
