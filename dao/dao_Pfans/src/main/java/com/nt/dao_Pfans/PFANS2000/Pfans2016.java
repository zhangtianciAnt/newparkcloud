package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "abnormal")
public class Pfans2016 extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 考勤异常ID
	 */
    @Id
    @Column(name = "ABNORMAI_ID")
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
    private DATETIME applicationdate;

    /**
     * 异常类别
     */
    @Column(name = "ERROR_TYPE")
    private String errortype;

    /**
     * 发生日期
     */
    @Column(name = "OCCURRENCE_DATE")
    private DATETIME occurrencedate;

    /**
     * 时间段（开始）
     */
    @Column(name = "PERIOD_START")
    private DATETIME periodstart;

    /**
     * 时间段（结束）
     */
    @Column(name = "PERIOD_END")
    private DATETIME periodend;

    /**
     * 事由
     */
    @Column(name = "CAUSE")
    private String cause;

    /**
     * 状态
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 创建时间
     */
    @Column(name = "CREATEON")
    private DATETIME createon;

    /**
     * 创建人
     */
    @Column(name = "CREATEBY")
    private String createby;

    /**
     * 更新时间
     */
    @Column(name = "MODIFYON")
    private DATETIME modifyon;

    /**
     * 更新人
     */
    @Column(name = "MODIFYBY")
    private String modifyby;

    /**
     * 负责人
     */
    @Column(name = "Owner")
    private String owner;
}
