package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "overtime")

public class Overtime extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 加班申请ID
     */
    @Id
    @Column(name = "OVERTIME_ID")
    private String overtimeid;

    /**
     * 申请人ID
     */
    @Column(name = "USER_ID")
    private String userid;

    /**
     * 所属グループID
     */
    @Column(name = "CENTER_ID")
    private String centerid;

    /**
     * 所属チームID
     */
    @Column(name = "GROUP_ID")
    private String groupid;

    /**
     * 所属センターID
     */
    @Column(name = "TEAM_ID")
    private String teamid;

    /**
     * 申请日期
     */
    @Column(name = "APPLICATION_DATE")
    private Date applicationdate;

    /**
     * 予定加班日期
     */
    @Column(name = "RESERVEOVERTIME_DATE")
    private Date reserveovertimedate;

    /**
     * 加班种类
     */
    @Column(name = "OVERTIME_TYPE")
    private String overtimetype;

    /**
     * 予定加班時間
     */
    @Column(name = "RESERVEOVER_TIME")
    private String reserveovertime;

    /**
     * 实际加班時間
     */
    @Column(name = "ACTUALOVER_TIME")
    private String actualovertime;

    /**
     * 预定代休日期
     */
    @Column(name = "RESERVESUBSTITUTION_DATE")
    private Date reservesubstitutiondate;

    /**
     * 实际代休日期
     */
    @Column(name = "ACTUALSUBSTITUTION_DATE")
    private Date actualsubstitutiondate;

    /**
     * 加班事由
     */
    @Column(name = "CAUSE")
    private String cause;

    /**
     * 承认状态
     */
    @Column(name = "RECOGNITIONSTATE")
    private String recognitionstate;
    /**
     * 加班时间合计
     */
    @Column(name = "WORKTIME")
    private String worktime;
}


