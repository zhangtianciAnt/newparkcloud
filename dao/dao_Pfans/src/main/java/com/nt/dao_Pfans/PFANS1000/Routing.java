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
@Table(name = "routing")
public class Routing extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *ルーティング申請ID
     */
    @Id
    @Column(name = "ROUTING_ID")
    private String routing_id;

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
     * 申請種別
     */
    @Column(name = "TYPESOF")
    private String typesof;

    /**
     * 操作種類
     */
    @Column(name = "OPERATIONTYPE")
    private String operationtype;

    /**
     * 申請日付
     */
    @Column(name = "payment")
    private String payment;

    /**
     * 内線番号
     */
    @Column(name = "EXTENSION")
    private String extension;

    /**
     * E－Mail
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * 开始期間
     */
    @Column(name = "DURINGDATE")
    private String duringdate;

    /**
     * 结束期間
     */
    @Column(name = "ENDDATE")
    private String enddate;

    /**
     * グループ責任者
     */
    @Column(name = "MANAGER")
    private String manager;

    /**
     * 申請理由
     */
    @Column(name = "REASON")
    private String reason;

    /**
     * ルーティング申請明细
     */
    @Column(name = "ROUTINGDETAIL_ID")
    private String routingdetail_id;

    /**
     * 通信方向
     */
    @Column(name = "COMMUNICATION")
    private String communication;

    /**
     * 接続元IPグループ
     */
    @Column(name = "SOURCEIPGROUP")
    private String sourceipgroup;

    /**
     * 接続元IPアドレス（PSDCD内）
     */
    @Column(name = "SOURCEIPADDRESS")
    private String sourceipaddress;

    /**
     * 接続先IPグループ
     */
    @Column(name = "DESTINATIONIPGROUP")
    private String destinationipgroup;

    /**
     * 接続先IPアドレス（PSDCD内）
     */
    @Column(name = "DESTINATIONIPADDRESS")
    private String destinationipaddress;

    /**
     * プロトコル又はポート番号
     */
    @Column(name = "PROTOCOL")
    private String protocol;

}
