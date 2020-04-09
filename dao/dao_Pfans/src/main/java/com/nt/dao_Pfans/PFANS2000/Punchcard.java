package com.nt.dao_Pfans.PFANS2000;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Punchcard {

    private String cardID;
    private String departmentID;
    private String departmentName;
    private String deviceID;
    private String deviceName;
    private String doorID;
    private String doorName;
    private String eventName;
    private String eventNo;
    private String recordID;
    private String recordTime;
    private String staffID;
    private String staffName;
    private String staffNo;
}
