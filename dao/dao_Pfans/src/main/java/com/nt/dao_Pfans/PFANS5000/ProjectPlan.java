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
@Table(name = "projectplan")
public class ProjectPlan extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 项目计划ID
     */
    @Id
    @Column(name = "PROJECTPLAN_ID")
    private String projectplan_id;

    /**
     * 所属项目
     */
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    /**
     * 作业项目
     */
    @Column(name = "PLANTYPE")
    private String plantype;

    /**
     * 预计工数（人月）
     */
    @Column(name = "NUMBERS")
    private String numbers;

    /**
     * 开始时间
     */
    @Column(name = "STARTTIME")
    private Date starttime;

    /**
     * 完成时间
     */
    @Column(name = "ENDTIME")
    private Date endtime;

    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
