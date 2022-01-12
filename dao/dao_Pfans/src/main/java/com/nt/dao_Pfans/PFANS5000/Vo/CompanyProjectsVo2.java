package com.nt.dao_Pfans.PFANS5000.Vo;

import com.nt.utils.Encryption.Encryption;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//PJ起案 现场管理添加筛选条件 ztc fr
public class CompanyProjectsVo2 extends BaseModel {
//    PJ起案 现场管理添加筛选条件 ztc to
    /**
     * 项目名称
     */
    @Encryption
    private String project_name;

    /**
     * 项目名称
     */
    @Encryption
    private String project_namejp;
    /**
     * 项目类型
     */
    private String projecttype;
    /**
     * GROUP
     */
    private String group_id;

    /**
     * 项目编号
     */
    private String numbers;

    /**
     * 工作阶段
     */
    private String phase;

    /**
     * 成果物状态
     */
    private String productstatus;

    /**
     * 阶段状态
     */
    private String phasestatus;


    /**
     * 预计工数
     */
    private String estimatedwork;

    /**
     * 实际工数
     */
    private String actualwork;


    /**
     * 合同状态
     */
    private String contractstatus;

    /**
     * 状态
     */
    private String status;

    /**
     * id
     */
    private String companyprojects_id;

    /**
     * 契約書番号
     */
    private String contractnumber;

    /**
     * 项目负责人(PL)
     */
    private String leaderid;

    /**
     * 项目分野
     */
    private String field;

    /**
     * 契約形式 todo
     */
//    private String contractnumber;

    /**
     * 受託工数
     */
    private String work;

    /**
     * 委托元
     */
    private String entrust;

    /**
     * 实际开始时间
     */
    private Date actualstarttime;

    /**
     * 实际结束时间
     */
    private Date actualendtime;

    /**
     * 负责人
     */
    private String owner;

    /**
     * 开始时间
     */
    private Date startdate;

    /**
     * 预计结束时间
     */
    private Date enddate;

    /**
     * 实际结束时间
     */
    private Date endtime;

    /**
     * 合同号
     */
    private String contractno;

    /**
     * 退场时间
     */
    private Date exittime;
}
