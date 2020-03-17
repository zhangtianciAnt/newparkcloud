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
@Table(name = "abnormal")
public class AbNormal extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 考勤异常ID
     */
    @Id
    @Column(name = "ABNORMAL_ID")
    private String abnormalid;

    /**
     * 所属长ID
     */
    @Column(name = "CENTER_ID")
    private String centerid;

    /**
     * 所属TLID
     */
    @Column(name = "GROUP_ID")
    private String groupid;

    /**
     * TeamID
     */
    @Column(name = "TEAM_ID")
    private String teamid;

    /**
     * 申请人ID
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 申请日期
     */
    @Column(name = "APPLICATION_DATE")
    private Date applicationdate;

    /**
     * 异常类别
     */
    @Column(name = "ERROR_TYPE")
    private String errortype;

    /**
     * 开始日
     */
    @Column(name = "OCCURRENCE_DATE")
    private Date occurrencedate;

    /**
     * 完了日
     */
    @Column(name = "FINISHED_DATE")
    private Date finisheddate;

    /**
     * 时间段（开始）
     */
    @Column(name = "PERIOD_START")
    private Date periodstart;

    /**
     * 时间段（结束）
     */
    @Column(name = "PERIOD_END")
    private Date periodend;

    /**
     * 时间长度
     */
    @Column(name = "LENGTHTIME")
    private String lengthtime;

    /**
     * 事由
     */
    @Column(name = "CAUSE")
    private String cause;

    /**
     * 妊娠诊断医院
     */
    @Column(name = "hospital")
    private String hospital;

    /**
     * 预产期
     */
    @Column(name = "edate")
    private Date edate;

    /**
     * 关联加班申请
     */
    @Column(name = "relation")
    private String relation;

    /**
     * 附件说明
     */
    @Column(name = "enclosureexplain")
    private String enclosureexplain;

    /**
     * 附件
     */
    @Column(name = "UPLOADFILE")
    private String uploadfile;

    /**
     * 承认状态
     */
    @Column(name = "RECOGNITIONSTATE")
    private String recognitionstate;

    /**
     * 代休扣除月份
     */
    @Column(name = "RESTDATE")
    private String restdate;

    /**
     * 开始日
     */
    @Column(name = "RE_OCCURRENCE_DATE")
    private Date reoccurrencedate;

    /**
     * 完了日
     */
    @Column(name = "RE_FINISHED_DATE")
    private Date refinisheddate;

    /**
     * 时间段（开始）
     */
    @Column(name = "RE_PERIOD_START")
    private Date reperiodstart;

    /**
     * 时间段（结束）
     */
    @Column(name = "RE_PERIOD_END")
    private Date reperiodend;

    /**
     * 时间长度
     */
    @Column(name = "RE_LENGTHTIME")
    private String relengthtime;
}
