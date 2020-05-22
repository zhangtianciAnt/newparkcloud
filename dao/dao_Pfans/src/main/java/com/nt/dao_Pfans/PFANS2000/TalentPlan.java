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
@Table(name = "talentplan")
public class TalentPlan extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 人材育成計画書ID
     */
    @Id
    @Column(name = "TALENTPLAN_ID")
    private String talentplan_id;

    /**
     * 姓名
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 所属センター
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 所属group
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 所属Team
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * スキルレベル
     */
    @Column(name = "SKILLLEVEL")
    private String skilllevel;

    /**
     * 校種
     */
    @Column(name = "SCHOOLSPECIES")
    private String schoolspecies;

    /**
     * 入社年
     */
    @Column(name = "ENTRYYEAR")
    private Date entryyear;

    /**
     * 卒業年
     */
    @Column(name = "GRADUATIONYEAR")
    private Date graduationyear;

    /**
     * 次回契約更新
     */
    @Column(name = "CONTRACT")
    private Date contract;

    /**
     * 担当業務
     */
    @Column(name = "BUSINESS")
    private String business;

    /**
     * 本人の人物・能力的特徴
     */
    @Column(name = "FEATURES")
    private String features;

    /**
     * 現有のスキルランク目安標準より1
     */
    @Column(name = "SKILLRANKING1")
    private String skillranking1;

    /**
     * 現有のスキルランク目安標準より2
     */
    @Column(name = "SKILLRANKING2")
    private String skillranking2;

    /**
     * 是否完成
     */
    @Column(name = "SKILLRANKINGFINISHED")
    private String skillrankingfinished;

    /**
     * 是否完成
     */
    @Column(name = "FUTUREFINISHED")
    private String futurefinished;

    /**
     * 次のスキルランク目安標準より1
     */
    @Column(name = "NEXTSKILLRANKING1")
    private String nextskillranking1;

    /**
     * 次のスキルランク目安標準より2
     */
    @Column(name = "NEXTSKILLRANKING2")
    private String nextskillranking2;

    /**
     * 是否完成
     */
    @Column(name = "NEXTSKILLRANKINGFINISHED")
    private String nextskillrankingfinished;

    /**
     * 今後１年間を想定し1
     */
    @Column(name = "FUTURE1")
    private String future1;

    /**
     * 今後１年間を想定し2
     */
    @Column(name = "FUTURE2")
    private String future2;

    /**
     * 技術系
     */
    @Column(name = "TECHNOLOGY")
    private String technology;

    /**
     * スタッフ系
     */
    @Column(name = "STAFF")
    private String staff;

    /**
     * 昇格･昇号時期
     */
    @Column(name = "YEARSCHEDULE")
    private Date yearschedule;

    /**
     * 昇格･昇号時期
     */
    @Column(name = "PROMOTION")
    private String promotion;

    /**
     * 年度
     */
    @Column(name = "YEAR")
    private String year;

}
