package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "psdcd")
public class Psdcd extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *PSDCD ID申請ID
     */
    @Id
    @Column(name = "PSDCD_ID")
    private String psdcd_id;

    /**
     * 所属センター
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 所属グループ
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 所属チーム
     */
    @Column(name = "TEAM_ID")
    private String team_id;


    /**
     * 申请人
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 申請種類
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * サブ種類
     */
    @Column(name = "SUBTYPE")
    private String subtype;

    /**
     * 申請日付
     */
    @Column(name = "APPLICATION")
    private String application;

    /**
     * 联系邮箱
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * 内缐
     */
    @Column(name = "EXTENSION")
    private String extension;

    /**
     * ID申請穜别
     */
    @Column(name = "IDTYPE")
    private String idtype;

    /**
     * PSDCD ID申請明细
     */
    @Column(name = "PSDCDDETAIL_ID")
    private String psdcddetail_id;

    /**
     * 番号
     */
    @Column(name = "NUMBER")
    private String number;

    /**
     * ユーザ種類
     */
    @Column(name = "USERTYPE")
    private String usertype;

    /**
     * ユーザ氏名
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * 姓
     */
    @Column(name = "SURNAME")
    private String surname;

    /**
     * 名
     */
    @Column(name = "MING")
    private String ming;

    /**
     * 元のメールアカウント
     */
    @Column(name = "ACCOUNT")
    private String account;

    /**
     * 社外送信
     */
    @Column(name = "TRANSMISSION")
    private String transmission;

    /**
     * 期待時間
     */
    @Column(name = "WAITFORTIME")
    private String waitfortime;

    /**
     * 预算单位
     */
    @Column(name = "BUDGETUNIT")
    private String budgetunit;

    /**
     * サイボウズAccount
     */
    @Column(name = "CYBOZU")
    private String cybozu;

    /**
     * A期待時間
     */
    @Column(name = "EXPECTTIME")
    private String expecttime;

    /**
     * Domain Account
     */
    @Column(name = "DOMAINACCOUNT")
    private String domainaccount;

    /**
     * D期待時間
     */
    @Column(name = "FORWARDTIME")
    private String forwardtime;

    /**
     * 備考
     */
    @Column(name = "PREPAREFOR")
    private String preparefor;



}
