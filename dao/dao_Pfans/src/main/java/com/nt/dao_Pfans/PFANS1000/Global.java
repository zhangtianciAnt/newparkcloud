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
@Table(name = "global")
public class Global extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * Global ID(EPOCH)申請
	 */
    @Id
    @Column(name = "GLOBAL_ID")
    private String global_id;

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
     * 申請日付
     */
    @Column(name = "PAYMENT")
    private Date payment;

    /**
     * 申請理由
     */
    @Column(name = "REASON")
    private String reason;

    /**
     * 申请センター
     */
    @Column(name = "APPCENTER_ID")
    private String appcenter_id;

    /**
     * 申请グループ
     */
    @Column(name = "APPGROUP_ID")
    private String appgroup_id;

    /**
     * 申请チーム
     */
    @Column(name = "APPTEAM_ID")
    private String appteam_id;

    /**
     * 使用者
     */
    @Column(name = "USER_NAME")
    private String user_name;

    /**
     * E-Mail
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * ローマ字氏名
     */
    @Column(name = "ROMANNAME")
    private String romanname;

    /**
     * 内線番号
     */
    @Column(name = "EXTENSION")
    private String extension;

    /**
     * 申請種別
     */
    @Column(name = "APPLICATIONTYPE")
    private String applicationType;

    /**
     * 使用Level
     */
    @Column(name = "USELEVEL")
    private String useLevel;
}
