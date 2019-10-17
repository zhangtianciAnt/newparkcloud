package com.nt.dao_Pfans.PFANS2000;

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
@Table(name = "goalmanagement")
public class GoalManagement extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 目标管理ID
	 */
    @Id
    @Column(name = "GOALMANAGEMENT_ID")
    private String goalmanagement_id;

    /**
     * 所属长ID
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 年度
     */
    @Column(name = "YEARS")
    private String years;

    /**
     * 氏名
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 阶段
     */
    @Column(name = "STAGE")
    private String stage;

    /**
     * 完成状态
     */
    @Column(name = "COMPLETIONSTATUS")
    private String completionstatus;

    /**
     * 作成時間
     */
    @Column(name = "MAKINGTIME")
    private Date makingtime;

    /**
     * 所属TLID
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * TeamID
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * 申请人ID
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 申请日期
     */
    @Column(name = "APPLICATION_DATE")
    private Date application_date;

    /**
     * ｽｷﾙﾗﾝｸ
     */
    @Column(name = "SKILL_RANK")
    private String skill_rank;

    /**
     * 役割
     */
    @Column(name = "ROLE")
    private String role;

    /**
     * ( １年間を通じての ) 主な目標
     */
    @Column(name = "SPECIFIC_GOALS_YEAR")
    private String specific_goals_year;

    /**
     * ( １年間を通じての ) 自己啓発目標
     */
    @Column(name = "YOUR_OWN_GOALS_YEAR")
    private String your_own_goals_year;

    /**
     * ( １年間を通じての ) 面談結果フードバック
     */
    @Column(name = "INTERVIEW_RESULTS_YEAR")
    private String interview_results_year;

    /**
     * 中間チェック ( 9月末時点 ) 主な目標
     */
    @Column(name = "SPECIFIC_GOALS_SEP")
    private String specific_goals_sep;

    /**
     * 中間チェック ( 9月末時点 ) 自己啓発目標
     */
    @Column(name = "YOUR_OWN_GOALS_SEP")
    private String your_own_goals_sep;

    /**
     * 中間チェック ( 9月末時点 ) 面談結果フードバック
     */
    @Column(name = "INTERVIEW_RESULTS_SEP")
    private String interview_results_sep;

    /**
     * 中間チェック ( 9月末時点 ) 上記以外の業務上の成果
     */
    @Column(name = "BUSINESS_RESULTS_SEP")
    private String business_results_sep;

    /**
     * ３/４年経過時点チェック ( 12月末時点 )主な目標
     */
    @Column(name = "SPECIFIC_GOALS_DEC")
    private String specific_goals_dec;

    /**
     * ３/４年経過時点チェック ( 12月末時点 )自己啓発目標
     */
    @Column(name = "YOUR_OWN_GOALS_DEC")
    private String your_own_goals_dec;

    /**
     * ３/４年経過時点チェック ( 12月末時点 )面談結果フードバック
     */
    @Column(name = "INTERVIEW_RESULTS_DEC")
    private String interview_results_dec;

    /**
     * ３/４年経過時点チェック ( 12月末時点 )上記以外の業務上の成果
     */
    @Column(name = "BUSINESS_RESULTS_DEC")
    private String business_results_dec;

    /**
     * 4/４最终确认( 3月末時点 ) 主な目標
     */
    @Column(name = "SPECIFIC_GOALS_MAR")
    private String specific_goals_mar;

    /**
     * 4/４最终确认( 3月末時点 ) 自己啓発目標
     */
    @Column(name = "YOUR_OWN_GOALS_MAR")
    private String your_own_goals_mar;

    /**
     * 4/４最终确认( 3月末時点 ) 面談結果フードバック
     */
    @Column(name = "INTERVIEW_RESULTS_MAR")
    private String interview_results_mar;

    /**
     * 4/４最终确认( 3月末時点 ) 上記以外の業務上の成果
     */
    @Column(name = "BUSINESS_RESULTS_MAR")
    private String business_results_mar;
}
