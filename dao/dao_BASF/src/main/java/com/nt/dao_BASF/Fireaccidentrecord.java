package com.nt.dao_BASF;

import cn.hutool.core.date.DateTime;
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
@Table(name = "fireaccidentrecord")
public class Fireaccidentrecord extends BaseModel {
    @Id
    private String fireaccidentrecordid;

    /**
     * 编号
     */
    private String fireaccidentno;

    /**
     * 接警单ID
     */
    private String firealarmid;
    /**
     * 接警单单号
     */
    private String firealarmno;
    /**
     * 指挥记录单id
     */
    private String commandrecordid;
    /**
     * 指挥记录单号
     */
    private String commandrecordno;
    /**
     * 事故时间
     */
    private Date accidenttime;
    /**
     * 事故地点
     */
    private String accidentlocation;
    /**
     * 经济损失
     */
    private String economicloss;
    /**
     * 事故原因
     */
    private String reason;
    /**
     * 伤亡情况
     */
    private String casualtysituation;
    /**
     * 处理经过
     */
    private String treatmentprocess;
    /**
     * 语音地址
     */
    private String voiceurl;
}