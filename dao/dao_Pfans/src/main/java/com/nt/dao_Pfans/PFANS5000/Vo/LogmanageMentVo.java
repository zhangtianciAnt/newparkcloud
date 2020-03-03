package com.nt.dao_Pfans.PFANS5000.Vo;

import com.nt.dao_Pfans.PFANS5000.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogmanageMentVo {

    /**
     * 项目管理
     */
    private List<CompanyProjects> companyProjects;
    /**
     * 日志管理
     */
    private List<LogManagement> logManagements;

}
