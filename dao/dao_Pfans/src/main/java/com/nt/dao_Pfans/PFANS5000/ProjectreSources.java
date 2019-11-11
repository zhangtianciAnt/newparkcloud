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
@Table(name = "projectresources")
public class ProjectreSources extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 项目计划ID
     */
    @Id
    @Column(name = "PROJECTRESOURCES_ID")
    private String projectresources_id;

    /**
     * 所属项目
     */
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    /**
     * 编号
     */
    @Column(name = "NUMBERS")
    private String numbers;

    /**
     * 姓名
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 角色
     */
    @Column(name = "ROLE")
    private String role;

    @Column(name = "ROWINDEX")
    private Integer rowindex;



}
