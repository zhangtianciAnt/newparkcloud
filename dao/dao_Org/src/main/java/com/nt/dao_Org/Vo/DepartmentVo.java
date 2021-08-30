package com.nt.dao_Org.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentVo {
    private String DepartmentId;
    private String Departmentname;
    private String Departmentshortname;
    private String DepartmentEn;
    private String DepartmentType;
    private String DepartmentUserid;
    private String DepartmentEncoding;
}
