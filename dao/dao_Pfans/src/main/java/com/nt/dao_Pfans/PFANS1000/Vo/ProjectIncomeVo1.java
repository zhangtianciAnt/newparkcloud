package com.nt.dao_Pfans.PFANS1000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectIncomeVo1 {
    private String companyproject;

    private String companyprojectid;

    private String contractamount;
    //0本月最后一次纳品项目，1不是本月最后一次纳品项目
    private String type;

    private String theme;
}
