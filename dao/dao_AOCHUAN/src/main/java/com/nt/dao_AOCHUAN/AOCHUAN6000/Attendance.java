package com.nt.dao_AOCHUAN.AOCHUAN6000;

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
@Table(name = "attendance")
public class Attendance extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //主键
    @Id
    @Column(name = "ATTENDANCE_ID")
    private String attendance_id;


    @Column(name = "ATTENDANCETIM")
    private String attendancetim;

    @Column(name = "JOBNUM")
    private String jobnum;


    @Column(name = "NAMES")
    private String names;


    @Column(name = "DEPARTMENT")
    private String department;


    @Column(name = "WORKINGHOURS")
    private String workinghours;


    @Column(name = "OFFHOURS")
    private String offhours;


    @Column(name = "LEAVES")
    private String leaves;


    @Column(name = "BELATE")
    private String belate;


    @Column(name = "LEAVEEARLY")
    private String leaveearly;


    @Column(name = "ABSENTEEISM")
    private String absenteeism;

}
