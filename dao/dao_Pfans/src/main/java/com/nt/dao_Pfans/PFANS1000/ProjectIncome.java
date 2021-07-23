package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projectincome")
public class ProjectIncome extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Column(name = "PROJECTINCOMEID")
    private String projectincomeid;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "MONTH")
    private String month;

    @Column(name = "GROUPID")
    private String group_id;

    @Column(name = "SUMTATOLE1")
    private String sumtatole1;

    @Column(name = "SUMTATOLE2")
    private String sumtatole2;

    @Column(name = "SUMTATOLE3")
    private String sumtatole3;

    @Column(name = "SUMTATOLE4")
    private String sumtatole4;

    @Column(name = "SUMTATOLE5")
    private String sumtatole5;

    @Column(name = "SUMTATOLE6")
    private String sumtatole6;

    @Column(name = "SUMTATOLE7")
    private String sumtatole7;

    @Column(name = "SUMTATOLE8")
    private String sumtatole8;

    @Column(name = "SUMTATOLE9")
    private String sumtatole9;


    @Column(name = "PROJECTINCOMEVO1")
    private String projectincomevo1;


    @Column(name = "PROJECTINCOMEVO2")
    private String projectincomevo2;


    @Column(name = "PROJECTINCOMEVO4")
    private String projectincomevo4;

    @Column(name = "PROJECTINCOMEVO5")
    private String projectincomevo5;

    @Column(name = "ENCODING")
    private String encoding;

}
