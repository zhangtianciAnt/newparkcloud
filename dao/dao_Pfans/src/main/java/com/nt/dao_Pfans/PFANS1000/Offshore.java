package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "offshore")
public class Offshore extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *境外出張事前面談票ID
     */
    @Id
    @Column(name = "OFFSHORE_ID")
    private String offshore_id;

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
     * 境外出張予定的名前
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 役職
     */
    @Column(name = "SERVICEPOSITION")
    private String serviceposition;

    /**
     * 出張事センター
     */
    @Column(name = "CENTERE_ID")
    private String centere_id;

    /**
     * 出張事グループ
     */
    @Column(name = "GROUPE_ID")
    private String groupe_id;

    /**
     * 出張事チーム
     */
    @Column(name = "TEAME_ID")
    private String teame_id;

    /**
     * 出張事的名前
     */
    @Column(name = "USER")
    private String user;

    /**
     * 面談日
     */
    @Column(name = "INTERVIEWDAY")
    private Date interviewday;

    /**
     * 情報セキュリティ面（PC持ち出しですか）
     */
    @Column(name = "INFORMATIONPC")
    private String informationpc;

    /**
     * 情報セキュリティ面（持ち出しの場合、保管注意喚起）
     */
    @Column(name = "INFORMATIONNO")
    private String informationno;

    /**
     * リスク管理面（緊急発生時、安全第一）
     */
    @Column(name = "MANAGEMENT")
    private String management;

    /**
     * 社員健康面（健康状態）
     */
    @Column(name = "HEALTHOFMEMBERS")
    private String healthofmembers;

    /**
     * 顧客職場マナー遵守・自律（PSDCD代表）
     */
    @Column(name = "CUSTOMERS")
    private String customers;

    /**
     * 対象国マナー・法律遵守（中国人代表）
     */
    @Column(name = "OBJECTCHINA")
    private String objectchina;

    /**
     * 目的
     */
    @Column(name = "OBJECTIVE")
    private String objective;

    /**
     * 期待すること
     */
    @Column(name = "LOOKFORWARD")
    private String lookforward;

    /**
     * これまでの病歴
     */
    @Column(name = "KOLEI")
    private String kolei;

    /**
     * 最近の病歴・現在の通院状況
     */
    @Column(name = "RECENTLY")
    private String recently;

    /**
     * 現在の服薬している薬
     */
    @Column(name = "NOW")
    private String now;

    /**
     * 緊急时名前
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 続柄
     */
    @Column(name = "SHANK")
    private String shank;

    /**
     * 住所
     */
    @Column(name = "ADDRESS")
    private String address;

    /**
     * 電話
     */
    @Column(name = "PHONENUMBER")
    private String phonenumber;

    /**
     * 事業場
     */
    @Column(name = "BUSINESSPLACE")
    private String businessplace;

    /**
     * 部署
     */
    @Column(name = "DEPLOY")
    private String deploy;

    /**
     * ドメイン
     */
    @Column(name = "DOMEI")
    private String domei;

    /**
     * 受入責任者
     */
    @Column(name = "RESPONSIBLEPERSON")
    private String responsibleperson;

    /**
     * 責任者役職・特称
     */
    @Column(name = "SPECIALCLASS")
    private String specialclass;

    /**
     * 責任者電話
     */
    @Column(name = "PHONE")
    private String phone;

    /**
     * 責任者e-mail
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * 担当人事氏名
     */
    @Column(name = "ACTASAPERSON")
    private String actasaperson;

    /**
     * 担当人役職・特称
     */
    @Column(name = "SPECIAL")
    private String special;

    /**
     * 担当人電話
     */
    @Column(name = "PHONENUM")
    private String phonenum;

    /**
     * 担当人e-mail
     */
    @Column(name = "MAIL")
    private String mail;

    //add-ws-7/7-禅道153
    /**
     * 出差申请关联
     */
    @Column(name = "BUSINESS_ID")
    private String business_id;
    //add-ws-7/7-禅道153
}
