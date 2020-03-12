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
@Table(name = "projectsystem")
public class Projectsystem extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 项目体制
     */
    @Id
    @Column(name = "PROJECTSYSTEM_ID")
    private String projectsystem_id;

    /**
     * 所属公司项目
     */
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    /**
     * 社内外协区分(0为社内1为外协)
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 编号
     */
    @Column(name = "NUMBER")
    private String number;

    /**
     * 协力公司
     */
    @Column(name = "COMPANY")
    private String company;

    /**
     * 姓名
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 外注人员ID
     */
    @Column(name = "SUPPLIERNAMEID")
    private String suppliernameid;

    /**
     * 职位
     */
    @Column(name = "POSITION")
    private String position;

    /**
     * 入场时间
     */
    @Column(name = "ADMISSIONTIME")
    private Date admissiontime;

    /**
     * 退场时间
     */
    @Column(name = "EXITTIME")
    private Date exittime;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
