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
@Table(name = "trialsoft")
public class Trialsoft extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *フリー・試用ソフト使用申請
     */
    @Id
    @Column(name = "TRIALSOFT_ID")
    private String trialsoft_id;

    /**
     * 申請種類
     */
    @Column(name = "TYPE")
    private String type;

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
     * 提出日付
     */
    @Column(name = "DAILYPAYMENT")
    private String dailypayment;

    /**
     * フリー・試用ソフト使用申請明细
     */
    @Column(name = "TRIALSOFTDETAIL_ID")
    private String trialsoftdetail_id;

    /**
     * 番号
     */
    @Column(name = "NUMBER")
    private String number;

    /**
     * 機器名
     */
    @Column(name = "MACHINENAME")
    private String machinename;

    /**
     * 利用者
     */
    @Column(name = "CUSTOMER")
    private String customer;

    /**
     * 利用開始日
     */
    @Column(name = "STARTDATE")
    private String startdate;

    /**
     * 利用終了日
     */
    @Column(name = "ENDDATE")
    private String enddate;

    /**
     * ソフトウェア名
     */
    @Column(name = "SOFTWARENAME")
    private String softwarename;

    /**
     * 性質
     */
    @Column(name = "NATURE")
    private String nature;

    /**
     * 開発者
     */
    @Column(name = "DEVELOPER")
    private String developer;

    /**
     * 用途
     */
    @Column(name = "EMPLOY")
    private String employ;

    /**
     * ソフトタイプ
     */
    @Column(name = "SOFTTYPE")
    private String softtype;

}
