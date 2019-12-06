package com.nt.dao_Pfans.PFANS2000;

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

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "ATTENDANCE_ID")
    private String attendance_id;

    @Column(name = "YEARS")
    private String years;

    @Column(name = "MONTHS")
    private String months;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "DATES")
    private Date dates;

    @Column(name = "NORMAL")
    private String normal;

//    @Column(name = "ACTUAL")
//    private String actual;

    @Column(name = "ORDINARYINDUSTRY")
    private String ordinaryindustry;

    @Column(name = "ORDINARYINDUSTRYNIGHT")
    private String ordinaryindustrynight;

    @Column(name = "WEEKENDINDUSTRY")
    private String weekendindustry;

    @Column(name = "WEEKENDINDUSTRYNIGHT")
    private String weekendindustrynight;

    @Column(name = "STATUTORYRESIDUE")
    private String statutoryresidue;

    @Column(name = "STATUTORYRESIDUENIGHT")
    private String statutoryresiduenight;

    @Column(name = "ANNUALRESTDAY")
    private String annualrestday;

    @Column(name = "SPECIALDAY")
    private String specialday;

    @Column(name = "YOUTHDAY")
    private String youthday;

    @Column(name = "WOMENSDAY")
    private String womensday;

    @Column(name = "SHORTSICKLEAVE")
    private String shortsickleave;

    @Column(name = "LONGSICKLEAVE")
    private String longsickleave;

    @Column(name = "COMPASSIONATELEAVE")
    private String compassionateleave;

    @Column(name = "ANNUALREST")
    private String annualrest;

    @Column(name = "DAIXIU")
    private String daixiu;

    @Column(name = "NURSINGLEAVE")
    private String nursingleave;

    @Column(name = "WELFARE")
    private String welfare;

    @Column(name = "LATE")
    private String late;

    @Column(name = "LEAVEEARLY")
    private String leaveearly;

    @Column(name = "ABSENTEEISM")
    private String absenteeism;

    @Column(name = "RECOGNITIONSTATE")
    private String recognitionstate;




}
