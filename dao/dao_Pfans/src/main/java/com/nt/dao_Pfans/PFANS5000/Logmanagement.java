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
public class Logmanagement extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 日志管理
     */
    @Id
    @Column(name = "LOGMANAGEMENT_ID")
    private String logmanagementid;

    /**
     * 日期
     */
    @Column(name = "LOG_DATE")
    private Date logdate;

    /**
     * 開始時間
     */
    @Column(name = "TIME_START")
    private Date timestart;

    /**
     * 結束時間
     */
    @Column(name = "TIME_END")
    private Date timeend;

    /**
     * 工作項目
     */
    @Column(name = "PROJECT_ID")
    private String projectid;

    /**
     * 工作階段
     */
    @Column(name = "WORK_PHASE")
    private String workphase;

    /**
     * 行為細分
     */
    @Column(name = "BEHAVIOR_BREAKDOWN")
    private String behaviorbreakdown;

    /**
     * 工作備註
     */
    @Column(name = "WORK_MEMO")
    private String workmemo;

    /**
     * 有無項目
     */
    @Column(name = "HAS_PROJECT")
    private String has_project;

    /**
     * 創建人
     */
    @Column(name = "CREATEBY")
    private String createby ;
    /**
     * 創建時間
     */
    @Column(name = "CREATEON")
    private Date createon;


    /**
     * 更新人
     */
    @Column(name = "MODIFYBY")
    private String modifyby;

    /**
     * 更新時間
     */
    @Column(name = "MODIFYON")
    private Date modifyon;

    /**
     * 負責人
     */
    @Column(name = "OWNER")
    private String owner;

    /**
     * 狀態
     */
    @Column(name = "STATUS")
    private String starts ;


}
