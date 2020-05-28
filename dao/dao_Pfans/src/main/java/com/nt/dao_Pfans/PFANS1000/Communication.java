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
@Table(name = "communication")
public class Communication  extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *交际费事前决裁愿ID
     */
    @Id
    @Column(name = "COMMUNICATION_ID")
    private String communication_id;

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
     * 姓名
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 参加理由
     */
    @Column(name = "REASON")
    private String reason;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 使用日期
     */
    @Column(name = "USEDATE")
    private Date usedate;

    /**
     * 公司名称
     */
    @Column(name = "COMPANY")
    private String company;

    /**
     * 参会人员
     */
    @Column(name = "PARTICIPANTS")
    private String participants;

    /**
     * 预定金额
     */
    @Column(name = "MONEYS")
    private String moneys;

    /**
     * 人均
     */
    @Column(name = "PERCAPITA")
    private String percapita;

    /**
     * 预算单位
     */
    @Column(name = "BUDGETUNIT")
    private String budgetunit;

    /**
     * 使用目的
     */
    @Column(name = "OBJECTIVE")
    private String objective;

    /**
     * 接待场所
     */
    @Column(name = "PLACE")
    private String place;
//add-ws-5/27-No.170
    /**
     * 编号
     */
    @Column(name = "NUMBERCATION")
    private String numbercation;
    //add-ws-5/27-No.170
}
