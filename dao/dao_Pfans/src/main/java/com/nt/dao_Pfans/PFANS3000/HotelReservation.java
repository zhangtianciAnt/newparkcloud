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
@Table(name = "hotelreservation")

public class HotelReservation extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 来客酒店预订ID
     */
    @Id
    @Column(name = "HOTELRESERVATION_ID")
    private String hotelreservationid;

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
     * 客人姓名（汉字）
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 客人姓名（罗马字）
     */
    @Column(name = "NAMEROME")
    private String namerome;

    /**
     * 酒店名称
     */
    @Column(name = "HOTEL")
    private String hotel;

    /**
     * 入住日期
     */
    @Column(name = "CHECKIN")
    private Date checkin;

    /**
     * 退房日期
     */
    @Column(name = "CHECKOUT")
    private Date checkout;

    /**
     * 住几日
     */
    @Column(name = "CHECKINDAYS")
    private String checkindays;

    /**
     * 是否吸烟
     */
    @Column(name = "SMOKE")
    private String smoke;

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

}
