package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: Riskassessments
 * @Author: 王哲
 * @Description: 风险研判信息（MySql表）
 * @Date: 2020/3/25 15:26
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "riskassessments")
public class Riskassessments extends BaseModel {
    /**
     * 风险判研信息主键
     */
    @Id
    @Column(name = "riskassessmentid")
    private String riskassessmentid;

    /**
     * 所属装置code
     */
    @Column(name = "devicecode")
    private String devicecode;

    /**
     * 日期
     */
    @Column(name = "date")
    private Date date;

    /**
     * 重大危险源
     */
    @Column(name = "majorrisk")
    private String majorrisk;

    /**
     * 重大监管危险化工工艺
     */
    @Column(name = "majorprocess")
    private String majorprocess;

    /**
     * 重点监管危险化学品
     */
    @Column(name = "majorregulatory")
    private String majorregulatory;

    /**
     * 装置总数
     */
    @Column(name = "devicenum")
    private Long devicenum;

    /**
     * 运行装置数
     */
    @Column(name = "rundevicenum")
    private Long rundevicenum;

    /**
     * 停产装置数
     */
    @Column(name = "stopdevicenum")
    private Long stopdevicenum;

    /**
     * 储罐总数
     */
    @Column(name = "vesselnum")
    private Long vesselnum;

    /**
     * 使用中的储罐数
     */
    @Column(name = "employvesslnum")
    private Long employvesslnum;

    /**
     * 停用中的储罐数
     */
    @Column(name = "stopvesselnum")
    private Long stopvesselnum;

    /**
     * 仓库总数
     */
    @Column(name = "warehousenum")
    private Long warehousenum;

    /**
     * 使用中的仓库数
     */
    @Column(name = "employwarehousenum")
    private Long employwarehousenum;

    /**
     * 停用中的仓库数
     */
    @Column(name = "stopwarehousenum")
    private Long stopwarehousenum;

    /**
     * 今日天气
     */
    @Column(name = "weather")
    private String weather;

    /**
     * 特殊动火作业数
     */
    @Column(name = "specialhotworknum")
    private Long specialhotworknum;

    /**
     * 一级动火作业数
     */
    @Column(name = "onelevelhotworknum")
    private Long onelevelhotworknum;

    /**
     * 二级动火作业数
     */
    @Column(name = "twolevelhotworknum")
    private Long twolevelhotworknum;

    /**
     * 受限空间作业数
     */
    @Column(name = "confinedspacenum")
    private Long confinedspacenum;

    /**
     * 断路作业数
     */
    @Column(name = "roadbreakingnum")
    private Long roadbreakingnum;

    /**
     * 动土作业数
     */
    @Column(name = "excavationnum")
    private Long excavationnum;

    /**
     * 盲板抽堵作业数
     */
    @Column(name = "blindplateoperationnum")
    private Long blindplateoperationnum;

    /**
     * 临时用电作业数
     */
    @Column(name = "temporaryelectricitynum")
    private Long temporaryelectricitynum;

    /**
     * 高处作业数
     */
    @Column(name = "workatheightnum")
    private Long workatheightnum;

    /**
     * 吊装作业数
     */
    @Column(name = "liftingoperation")
    private String liftingoperation;

    /**
     * 是否有承包商作业
     */
    @Column(name = "contractoroperating")
    private String contractoroperating;

    /**
     * 是否有试生产
     */
    @Column(name = "pilotproduction")
    private String pilotproduction;

    /**
     * 是否处于开停车状态
     */
    @Column(name = "operationandstop")
    private String operationandstop;

    /**
     * 是否采取极端天气措施
     */
    @Column(name = "extremeweathermeasures")
    private String extremeweathermeasures;

    /**
     * 是否执行节假日领导带班制度
     */
    @Column(name = "holidaysduty")
    private String holidaysduty;

    /**
     * 是否具备足够的安全知识、操作技能和应急处置能力
     */
    @Column(name = "disposition")
    private String disposition;

    /**
     * 是否处于应战备战状态
     */
    @Column(name = "war")
    private String war;

    /**
     * 工艺参数是否处于正常指标范围
     */
    @Column(name = "technologicalparameter")
    private String technologicalparameter;

    /**
     * 是否处于安全运行状态
     */
    @Column(name = "safetyoperation")
    private String safetyoperation;

    /**
     * 密封无泄漏
     */
    @Column(name = "seal")
    private String seal;

    /**
     * 各类安全设施配备是否完好投用
     */
    @Column(name = "facilityintact")
    private String facilityintact;

    /**
     * 储罐、管道、机泵、阀门及仪表系统是否完好
     */
    @Column(name = "vesselintact")
    private String vesselintact;

    /**
     * 液位、温度、压力是否处于正常指标范围
     */
    @Column(name = "indicatornormal")
    private String indicatornormal;

    /**
     * 内浮顶储罐运行中浮盘是否无落底
     */
    @Column(name = "landfall")
    private String landfall;

    /**
     * 自动控制仪表、有毒可燃气体检测报警和联锁是否处于可靠运行状态
     */
    @Column(name = "reliabilityservice")
    private String reliabilityservice;

    /**
     * 是否按国家和上海市仓储定置管理要求分区分类储存危险化学品
     */
    @Column(name = "classifiedstorage")
    private String classifiedstorage;

    /**
     * 不存在超量、超品种储存
     */
    @Column(name = "extrastorage")
    private String extrastorage;

    /**
     * 相互禁配物质无混放混存
     */
    @Column(name = "mixedstorage")
    private String mixedstorage;

    /**
     * 有毒可燃气体检测报警是否处于可靠运行状态
     */
    @Column(name = "combustiblegasdetector")
    private String combustiblegasdetector;

    /**
     * 装置开停车是否制定开停车方案并正确实施
     */
    @Column(name = "programimplementation")
    private String programimplementation;

    /**
     * 试生产是否制定方案并经专家论证
     */
    @Column(name = "expertargumentation")
    private String expertargumentation;

    /**
     * 特殊作业、检维修作业、承包商作业是否健全和完善相关管理制度
     */
    @Column(name = "managementsystem")
    private String managementsystem;

    /**
     * 作业过程是否进行危险有害因素辨识
     */
    @Column(name = "harmfuldiscern")
    private String harmfuldiscern;

    /**
     * 是否严格执行程序确认和作业许可审批
     */
    @Column(name = "licenseapproval")
    private String licenseapproval;

    /**
     * 危险化学品罐区动火作业是否做到升级管理等
     */
    @Column(name = "upgradeadmin")
    private String upgradeadmin;

    /**
     * 各项变更的审批程序是否符合规定
     */
    @Column(name = "changerules")
    private String changerules;

    /**
     * 是否落实管控及降低风险措施
     */
    @Column(name = "riskreduction")
    private String riskreduction;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
}
