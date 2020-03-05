package com.nt.dao_Pfans.PFANS5000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProjectsVo3 {
    /**
     * 项目名称
     */
    private String project_name;

    /**
     * 项目类型
     */
    private String projecttype;

    /**
     * 部门
     */
    private String departmentid;

    /**
     * 成果物状态
     */
    private String name;

    /**
     * 开始时间
     */
    private String mintime;


    /**
     * 结束时间
     */
    private String maxtime;


}
