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
    @Id
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    /**
     * 社内外协区分
     */
    @Id
    @Column(name = "TYPE")
    private String type;

    /**
     * 编号
     */
    @Id
    @Column(name = "NUMBER")
    private String number;

    /**
     * 协力公司
     */
    @Id
    @Column(name = "COMPANY")
    private String company;

    /**
     * 姓名
     */
    @Id
    @Column(name = "NAME")
    private String name;

    /**
     * 职位
     */
    @Id
    @Column(name = "POSITION")
    private String position;

    /**
     * 入场时间
     */
    @Id
    @Column(name = "ADMISSIONTIME")
    private Date admissiontime;

    /**
     * 退场时间
     */
    @Id
    @Column(name = "EXITTIME")
    private Date exittime;

}
