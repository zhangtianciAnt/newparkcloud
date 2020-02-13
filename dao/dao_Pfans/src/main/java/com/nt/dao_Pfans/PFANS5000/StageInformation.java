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
@Table(name = "stageinformation")
public class StageInformation extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 项目开发计划主键id
     */
    @Id
    @Column(name = "STAGEINFORMATION_ID")
    private String stageinformation_id;

    /**
     * 主表外键
     */
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    /**
     * 工作阶段
     */
    @Column(name = "PHASE")
    private String phase;

    /**
     * 阶段状态
     */
    @Column(name = "PHASESTATUS")
    private String phasestatus;

    /**
     * 阶段成果物
     */
    @Column(name = "STAGEPRODUCT")
    private String stageproduct;

    /**
     * 成果物状态
     */
    @Column(name = "PRODUCTSTATUS")
    private String productstatus;

    /**
     * 预计工数（人月）
     */
    @Column(name = "ESTIMATEDWORK")
    private String estimatedwork;

    /**
     * 实际工数（人月）
     */
    @Column(name = "ACTUALWORK")
    private String actualwork;

    /**
     * 预计开始时间
     */
    @Column(name = "ESTIMATEDSTARTTIME")
    private Date estimatedstarttime;

    /**
     * 预计结束时间
     */
    @Column(name = "ESTIMATEDENDTIME")
    private Date estimatedendtime;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 实际开始时间
     */
    @Column(name = "ACTUALSTARTTIME")
    private Date actualstarttime;

    /**
     * 实际结束时间
     */
    @Column(name = "ACTUALENDTIME")
    private Date actualendtime;

    /**
     * 成果物
     */
    @Column(name = "PRODUCT")
    private String product;

    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
