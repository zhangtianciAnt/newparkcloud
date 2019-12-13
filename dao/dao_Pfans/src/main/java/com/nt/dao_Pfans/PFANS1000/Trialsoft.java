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
    private Date dailypayment;

}
