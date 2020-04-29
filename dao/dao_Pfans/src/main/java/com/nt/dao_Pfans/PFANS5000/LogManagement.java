package com.nt.dao_Pfans.PFANS5000;

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
@Table(name = "logmanagement")
public class LogManagement extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 日志管理
     */
    @Id
    @Column(name = "LOGMANAGEMENT_ID")
    private String logmanagement_id;

    /**
     * GROUP
     */
    @Id
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 日期
     */
    @Column(name = "LOG_DATE")
    private Date log_date;

    /**
     * 開始時間
     */
    @Column(name = "TIME_START")
    private String time_start;


    @Column(name = "WBS_ID")
    private String wbs_id;

    /**
     * 工作項目
     */
    @Column(name = "PROJECT_ID")
    private String project_id;

    /**
     * 工作項目名
     */
    @Column(name = "PROJECT_NAME")
    private String project_name;

    /**
     * 工作階段
     */
    @Column(name = "WORK_PHASE")
    private String work_phase;

    /**
     * 行為細分
     */
    @Column(name = "BEHAVIOR_BREAKDOWN")
    private String behavior_breakdown;

    /**
     * 工作備註
     */
    @Column(name = "WORK_MEMO")
    private String work_memo;

    /**
     * 有無項目
     */
    @Column(name = "HAS_PROJECT")
    private String has_project;


    @Column(name = "JOBNUMBER")
    private String jobnumber;

    @Column(name = "CONFIRMSTATUS")
    private String confirmstatus;

}
