package com.nt.dao_Pfans.PFANS1000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentTotalVo {
    private String USERID;
    private String DEPARTMENT;
    private String LOGDATE;
    private String COMPANYPROJECTS_ID;
    private BigDecimal MOUNT;
    private String TYPE;

}
