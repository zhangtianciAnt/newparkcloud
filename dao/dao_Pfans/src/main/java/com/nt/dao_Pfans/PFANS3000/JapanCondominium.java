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
@Table(name = "japancondominium")

public class JapanCondominium extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 日本出張のマンション予約ID
     */
    @Id
    @Column(name = "JAPANCONDOMINIUM_ID")
    private String japancondominiumid;

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
     * 予算番号
     */
    @Column(name = "NUMBER")
    private String number;

    /**
     * 类别
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 出張都市
     */
    @Column(name = "BUSINESSCITY")
    private String businesscity;

    /**
     * 出張会社場所
     */
    @Column(name = "TRAVELCLUBPLACE")
    private String travelclubplace;

    /**
     * マンション場所
     */
    @Column(name = "MANSIONLOCATION")
    private String mansionlocation;

    /**
     * 部屋番号
     */
    @Column(name = "HOUSENUMBER")
    private String housenumber;

    /**
     * 契約開始日
     */
    @Column(name = "CONTRACTSTARTDATE")
    private Date contractstartdate;

    /**
     * 契約終了日
     */
    @Column(name = "CONTRACTENDDATE")
    private Date contractenddate;

    /**
     * 契約日数
     */
    @Column(name = "CONTRACTDATENUMBER")
    private String contractdatenumber;

    /**
     * 出張開始日
     */
    @Column(name = "BUSINESSSTARTDATE")
    private Date businessstartdate;

    /**
     * 出張終了日
     */
    @Column(name = "BUSINESSENDDATE")
    private Date businessenddate;

    /**
     * 出張日数
     */
    @Column(name = "BUSINESSDATENUMBER")
    private String businessdatenumber;


    /**
     * 契約対応時間
     */
    @Column(name = "CONTRACTTIME")
    private Date contracttime;

    /**
     * 総費用(円)
     */
    @Column(name = "TOTALCOST")
    private String totalcost;

    /**
     * 費用/日(円)
     */
    @Column(name = "DAILYCOST")
    private String dailycost;

    /**
     * 残劵
     */
    @Column(name = "COPUNTYPE")
    private String copuntype;

    /**
     * 残劵数量
     */
    @Column(name = "COPUNNUMBER")
    private String copunnumber;

    /**
     * 抵值金额
     */
    @Column(name = "COPUNVALUE")
    private String copunvalue;

    /**
     * 使用数量
     */
    @Column(name = "USENUMBER")
    private String usenumber;

    /**
     * 残金金额
     */
    @Column(name = "MONEY")
    private String money;

    /**
     * 使用金额
     */
    @Column(name = "USEMONEY")
    private String usemoney;

    /**
     * 另支付金额
     */
    @Column(name = "PAYMONEY")
    private String paymoney;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;


}
