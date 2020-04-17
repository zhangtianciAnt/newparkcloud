package com.nt.dao_Pfans.PFANS3000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointmentcar")

public class AppointmentCar extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 名片申请ID
     */
    @Id
    @Column(name = "APPOINTMENTCAR_ID")
    private String appointmentcarid;

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
     * 使用日期
     */
    @Column(name = "USEDATE")
    private Date usedate;

    /**
     * 预约人手机号
     */
    @Column(name = "MOBILEPHONE")
    private String mobilephone;

    /**
     * 使用类型
     */
    @Column(name = "USETYPE")
    private String usetype;

    /**
     * 出发地
     */
    @Column(name = "ORIGIN")
    private String origin;

    /**
     * 目的地（中转）
     */
    @Column(name = "TRANSFERSTATION")
    private String transferstation;

    /**
     * 目的地（最终）
     */
    @Column(name = "DESTINATION")
    private String destination;

    /**
     * 使用开始时间
     */
    @Column(name = "STARTTIME")
    private Date starttime;

    /**
     * 使用结束时间（预计）
     */
    @Column(name = "ENDTIME")
    private Date endtime;

    /**
     * 航班号
     */
    @Column(name = "FLIGHTNUMBER")
    private String flightnumber;

    /**
     * 国际/国内
     */
    @Column(name = "DISTINGUISH")
    private String distinguish;

    /**
     * 飞机起飞城市
     */
    @Column(name = "DEPARTURECITY")
    private String departurecity;

    /**
     * 接机提示牌
     */
    @Column(name = "WELCOMEBOARD")
    private String welcomeboard;

    /**
     * 社員同行
     */
    @Column(name = "FELLOWMEMBERS")
    private String fellowmembers;

    /**
     * 社员名
     */
    @Column(name = "FELLOWMEMBERSNAME")
    private String fellowmembersname;

    /**
     * 客人名字
     */
    @Column(name = "GUESTNAME")
    private String guestname;

    /**
     * 使用人数
     */
    @Column(name = "USENUMBER")
    private String usenumber;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 申请日期
     */
    @Column(name = "APPLICATIONDATE")
    private Date applicationdate;

    /**
     * 是否受理
     */
    @Column(name = "ACCEPT")
    private String accept;

    /**
     * 受理状态
     */
    @Column(name = "ACCEPTSTATUS")
    private String acceptstatus;

    /**
     * 完成日期
     */
    @Column(name = "FINDATE")
    private Date findate;

    /**
     * 拒绝理由
     */
    @Column(name = "REFUSEREASON")
    private String refusereason;


}