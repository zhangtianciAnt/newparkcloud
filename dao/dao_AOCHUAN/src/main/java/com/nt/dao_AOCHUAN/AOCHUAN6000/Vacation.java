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
@Table(name = "vacation")
public class Vacation extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //主键
    @Id
    @Column(name = "VACATION_ID")
    private String vacation_id;


    @Column(name = "DEPARTMENT")
    private String department;


    @Column(name = "APPLICANTER")
    private String applicanter;

    @Column(name = "APPLICANTERID")
    private String applicanterid;


    @Column(name = "APPLICATIONTIM")
    private Date applicationtim;


    @Column(name = "APPLICATIONTYPE")
    private String applicationtype;


    @Column(name = "STARTDATE")
    private Date startdate;


    @Column(name = "ENDDATE")
    private Date enddate;


    @Column(name = "STARTTIM")
    private String starttim;


    @Column(name = "ENDTIM")
    private String endtim;


    @Column(name = "SUMTIM")
    private String sumtim;

    @Column(name = "ANNEX")
    private String annex;

    @Column(name = "REMARKS")
    private String remarks;


    @Column(name = "ANNUALYEAR")
    private String annualyear;


    @Column(name = "DATETYPE")
    private String datetype;




}
