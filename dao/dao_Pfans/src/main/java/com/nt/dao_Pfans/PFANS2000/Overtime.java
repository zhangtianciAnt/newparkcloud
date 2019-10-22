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
    private String user_id;

    /**
     * 所属グループID
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 所属チームID
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 所属センターID
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * 申请日期
     */
    @Column(name = "APPLICATION_DATE")
    private Date application_date;

    /**
     * 予定加班日期
     */
    @Column(name = "RESERVEOVERTIME_DATE")
    private Date reserveovertime_date;

    /**
     * 加班种类
     */
    @Column(name = "OVERTIME_TYPE")
    private String overtime_type;

    /**
     * 予定加班時間
     */
    @Column(name = "RESERVEOVER_TIME")
    private String reserveover_time;

    /**
     * 实际加班時間
     */
    @Column(name = "ACTUALOVER_TIME")
    private String actualover_time;

    /**
     * 预定代休日期
     */
    @Column(name = "RESERVESUBSTITUTION_DATE")
    private Date reservesubstitution_date;

    /**
     * 实际代休日期
     */
    @Column(name = "ACTUALSUBSTITUTION_DATE")
    private Date actualsubstitution_date;

    /**
     * 加班事由
     */
    @Column(name = "CAUSE")
    private String cause;




}


