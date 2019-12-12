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
    private Date payment;

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
     * 期間
     */
    @Column(name = "DURINGDATE")
    private String duringdate;

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

}
