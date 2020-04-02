package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "residual")
public class Residual extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "RESIDUAL_ID")
    private String residual_id;

    @Column(name = "GIVING_ID")
    private String giving_id;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "RN")
    private String rn;

    @Column(name = "LASTWEEKDAYS")
    private String lastweekdays;

    @Column(name = "lastrestday")
    private String lastrestDay;

    @Column(name = "LASTLEGAL")
    private String lastlegal;

    @Column(name = "LASTREPLACE")
    private String lastreplace;

    @Column(name = "LASTLATENIGHT")
    private String lastlatenight;

    @Column(name = "LASTRESTLATENIGHT")
    private String lastrestlatenight;

    @Column(name = "LASTLEGALLATENIGHT")
    private String lastlegallatenight;

    @Column(name = "LASTTOTALH")
    private String lasttotalh;

    @Column(name = "LASTTOTALY")
    private String lasttotaly;

    @Column(name = "THISWEEKDAYS")
    private String thisweekdays;

    @Column(name = "THISRESTDAY")
    private String thisrestDay;

    @Column(name = "THISLEGAL")
    private String thislegal;

    @Column(name = "THISREPLACE")
    private String thisreplace;

    @Column(name = "THISREPLACE3")
    private String thisreplace3;

    @Column(name = "THISLATENIGHT")
    private String thislatenight;

    @Column(name = "THISRESTLATENIGHT")
    private String thisrestlatenight;

    @Column(name = "THISLEGALLATENIGHT")
    private String thislegallatenight;

    @Column(name = "THISTOTALH")
    private String thistotalh;

    @Column(name = "THISTOTALY")
    private String thistotaly;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "SUBSIDY")
    private String subsidy;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

    @Column(name = "JOBNUMBER")
    private String jobnumber;

    @Transient
    private boolean isDirty;    // 判断该条记录是否被修改
}
