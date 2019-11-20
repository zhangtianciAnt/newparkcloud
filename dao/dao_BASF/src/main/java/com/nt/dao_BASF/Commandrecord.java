package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: Information
 * @Description: 前端大屏消防接警数据表
 * @Author: sunxu
 * @CreateDate: 2019/11/14
 * @Version: 1.0
 */

@Document(collection = "commandrecord")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commandrecord extends BaseModel {

    /**
     * 数据主键ID
     */
    private String _id;
    //消防报警单的主键
    private String firealarmid;
    //消防报警单编号
    private String firealarmno;
    //消防接警单编号
    private String commandrecordno;
    //指挥类型，0正常，1演习
    private String commandType;
    //事故指挥日期
    private String currentDate;
    //值班人员
    private String watchkeeper;
    //值班SIMT
    private String watchkeeperSimt;
    //所有车辆信息
    private Carinfo carinfo;
    //医疗
    private MedicalCare medicalCare;
    //scip
    private Scip scip;
    //报警类型
    private AlarmType alarmType;
    //人员伤亡
    private Casualties casualties;
    //相关化学品
    private RelatedRelatedchemicals relatedRelatedchemicals;
    //事故地点
    private String accidentlocation;
    //报警人
    private String alarmpeople;
    //回复电话
    private String alarmphone;
    //事故指挥人
    private List<AccidentCommand> accidentCommand;
    //内部通知
    private InternalNotification internalNotification;
    //SCIP通报
    private String scipBulletin;
    //应急处置
    private List<EmergencyDisposal> emergencyDisposal;
    //指挥指令
    private List<Command> command;
    //事故状态解除
    private ReleaseAccident releaseAccident;

    //    SCIP
    @Data
    public static class Scip {
        //应急响应中心
        private String emergencyCenter;
        //化工公安
        private String chemicalSecurity;
        //化工消防
        private String chemicalFire;
        //管委会
        private String managementCommittee;
        //其他
        private String other;
    }

    //报警类型
    @Data
    public static class AlarmType {
        //火灾
        private String fire;
        //泄露
        private String leak;
        //毒气泄漏
        private String gasLeak;
        //受伤
        private String Injured;
        //其他
        private String other;
    }

    //人员伤亡
    @Data
    public static class Casualties {
        //存在，0代表无，1代表有
        private String existence;
        //伤亡人数
        private String number;
    }

    //相关化学品
    @Data
    public static class RelatedRelatedchemicals {
        //化学品名称
        private String chemicalsName;
        //化学品数量
        private String chemicalsNumber;
    }

    //事故指挥人
    @Data
    public static class AccidentCommand {
        //指挥人姓名
        private String name;
        //设定指挥的时间
        private String time;
    }

    //内部通知
    @Data
    public static class InternalNotification {

        private String simt;
        private String ehs;
        private String plant;
        private String em;
        private String others;
    }

    //事故状态解除
    @Data
    public static class ReleaseAccident {
        //解除时间
        private String time;
        //火灾已完全扑灭的时间
        private String fireSuppression;
        //泄漏点已消除的时间
        private String leakageEliminated;
        //危险化学品已得到控制的时间
        private String chemicAlscontrol;
        //受伤人员已妥善处置的时间
        private String injuredDisposed;
        //现场洗消已结束的时间
        private String fieldCleaning;
        //环境监测合格
        private EnvironmentalQualification environmentalQualification;
        //事故现场已保护
        private String accidentProtected;
    }

    //环境监测合格
    @Data
    public static class EnvironmentalQualification {
        //环境监测合格时间
        private String time;
        //气合格时间
        private String gasEnvironmental;
        //水合格时间
        private String waterEnvironmental;
    }

    //指挥指令
    @Data
    public static class Command extends BaseModel {
        //发布指令时间
        private String time;
        //发布人
        private String publisher;
        //指令
        private String mark;
    }

    //应急处置
    @Data
    public static class EmergencyDisposal extends BaseModel {
        //时间
        private String time;
        //处置措施
        private String mark;
    }

    //医疗
    @Data
    public static class MedicalCare {
        //基地医生
        private Doctor doctor;
        //医疗中心
        private Doctor scipMc;
    }

    @Data
    public static class Doctor {
        //已通知时间
        private String notice;
        //已到达时间
        private String arrive;
    }

    //车辆信息
    @Data
    public static class Car {
        //出发时间
        private String setOut;
        //到达时间
        private String arrive;
        //返回时间
        private String goBack;
        //车辆状态，0启用，1停用
        private String state;
        //代表ffs或SGs
        private String ffs;
    }

    //所有车辆信息
    @Data
    public static class Carinfo {
        private Car car1;
        private Car car2;
        private Car car3;
        private Car car4;
        private Car car5;
        private Car car6;
        private Car car7;
        private Car car8;
    }
}
