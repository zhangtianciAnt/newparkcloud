package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.tracking.TrackerBoosting;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.naming.InterruptedNamingException;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: Information
 * @Description: 前台接警指挥界面数据表
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
    private String firealarmid;//火灾接警单主键
    private String firealarmno;//火灾接警单编号
    private String commandrecordno;//指挥单单号
    private String commandType;//指挥类型，0正常，1演习
    private String currentDate;//事故指挥日期
    private String watchkeeper;//值班人员
    private String watchkeeperSimt;//值班SIMT
    private Carinfo carinfo;//所有车辆信息
    private MedicalCare medicalCare;//医疗
    private Scip scip;//scip
    private AlarmType alarmType;//报警类型
    private Casualties casualties;//人员伤亡
    private RelatedRelatedchemicals relatedRelatedchemicals;//相关化学品
    private String accidentlocation;//事故地点
    private String alarmpeople;//报警人
    private String alarmphone;//回复电话
    private List<AccidentCommand> accidentCommand;//事故指挥人
    private InternalNotification internalNotification;//内部通知
    private String scipBulletin;//SCIP通报
    private List<EmergencyDisposal> emergencyDisposal;//应急处置
    private List<Command> command;//指挥指令
    private ReleaseAccident releaseAccident;//事故状态解除

    @Data
    public static class Scip {
        //SCIP
        private String emergencyCenter;//应急响应中心
        private String chemicalSecurity;//化工公安
        private String chemicalFire;//化工消防
        private String managementCommittee;//管委会
        private String other;//其他
    }

    @Data
    public static class AlarmType {
        //报警类型
        private String fire;//火灾
        private String leak;//泄露
        private String gasLeak;//毒气泄漏
        private String Injured;//受伤
        private String other;//其他
    }

    @Data
    public static class Casualties {
        //人员伤亡
        private String existence;//存在，0代表无，1代表有
        private String number;//伤亡人数
    }

    @Data
    public static class RelatedRelatedchemicals {
        //相关化学品
        private String chemicalsName;//化学品名称
        private String chemicalsNumber;//化学品数量
    }

    @Data
    public static class AccidentCommand {
        //事故指挥人
        private String name;//指挥人姓名
        private String time;//设定指挥的时间
    }

    @Data
    public static class InternalNotification {
        //内部通知
        private String simt;
        private String ehs;
        private String plant;
        private String em;
        private String others;
    }

    @Data
    public static class ReleaseAccident {
        //事故状态解除
        private String time;//解除时间
        private String fireSuppression;//火灾已完全扑灭的时间
        private String leakageEliminated;//泄漏点已消除的时间
        private String chemicAlscontrol;//危险化学品已得到控制的时间
        private String injuredDisposed;//受伤人员已妥善处置的时间
        private String fieldCleaning;//现场洗消已结束的时间
        private EnvironmentalQualification environmentalQualification;//环境监测合格
        private String accidentProtected;//事故现场已保护
    }

    @Data
    public static class EnvironmentalQualification {
        //环境监测合格
        private String time;//环境监测合格时间
        private String gasEnvironmental;//气合格时间
        private String waterEnvironmental;//水合格时间
    }

    @Data
    public static class Command extends BaseModel {
        //指挥指令
        private String time;//发布指令时间
        private String publisher;//发布人
        private String mark;//指令
    }

    @Data
    public static class EmergencyDisposal extends BaseModel {
        //应急处置
        private String time;//时间
        private String mark;//处置措施
    }

    @Data
    public static class MedicalCare {
        //医疗
        private Doctor doctor;//基地医生
        private Doctor scipMc;//医疗中心
    }

    @Data
    public static class Doctor {
        private String notice;//已通知时间
        private String arrive;//已到达时间
    }

    @Data
    public static class Car {
        //车辆信息
        private String setOut;//出发时间
        private String arrive;//到达时间
        private String goBack;//返回时间
        private String state;//车辆状态
        private String FFs;//代表FFs或SGs
    }

    @Data
    public static class Carinfo {
        //所有车辆信息
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
