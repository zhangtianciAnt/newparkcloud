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
@Table(name = "firealarm")
public class Firealarm extends BaseModel {

    @Id
    private String firealarmid;

    /**
     * 报警人
     */
    private String alarmpeo;

    /**
     * 报警时间
     */
    private String alarmtimes;

    /**
     * 回复电话
     */
    private String replypho;

    /**
     * 事故单位
     */
    private String accunit;

    /**
     * 风向
     */
    private String windirection;

    /**
     * 具体地点
     */
    private String speloc;

    /**
     * 所在装置
     */
    private String indevice;

    /**
     * 人员伤亡
     */
    private String casualties;

    /**
     * 事故类型
     */
    private String typacc;

    /**
     * 化学品数量
     */
    private String chemno;

    /**
     * 化学品名称
     */
    private String chemname;

    /**
     * 出警时间
     */
    private String policetim;

    /**
     * 事故发生时间
     */
    private String acctime;

    /**
     * 接车地点
     */
    private String pickloc;

    /**
     * 接车员联系方式
     */
    private String pickpho;

    /**
     * 探测类型
     */
    private String protype;

    /**
     * 探测器编号
     */
    private String prono;

    /**
     * 应急预案选择
     */
    private String emplan;

    /**
     * 报警等级
     */
    private String alarmlev;

    /**
     * 短信模板
     */
    private String smsmod;

    /**
     * 注释
     */
    private String remarks;

    /**
     * 接警单编号
     */
    private String firealarmno;


    /**
     * 接警单状态 0:未完成;1：完成
     */
    private String completesta;

    /**
     * 是否误报 0：非误报；1：误报
     */
    private String misinformation;


}
