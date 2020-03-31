package com.nt.dao_Pfans.PFANS5000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogmanagementVo2 {
    private String COMPANYPROJECTS_ID;

    private Date estimatedendtime;

    private Date estimatedstarttime;

    private Date extensiondate;

    private Date claimdatetime;
}
