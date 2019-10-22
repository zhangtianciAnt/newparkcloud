package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private String userid;

    /**
     * 申请日期
     */
    @Column(name = "APPLICATION_DATE")
    private String applicationdate;

    /**
     * 异常类别
     */
    @Column(name = "ERROR_TYPE")
    private String errortype;

    /**
     * 开始日
     */
    @Column(name = "OCCURRENCE_DATE")
    private String occurrencedate;

    /**
     * 完了日
     */
    @Column(name = "FINISHED_DATE")
    private String finisheddate;

    /**
     * 时间段（开始）
     */
    @Column(name = "PERIOD_START")
    private String periodstart;

    /**
     * 时间段（结束）
     */
    @Column(name = "PERIOD_END")
    private String periodend;

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
}
