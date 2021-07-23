package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "incomeexpenditure")
public class IncomeExpenditure extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "INCOMEEXPENDITURE_ID")
    private String incomeexpenditure_id;


    @Column(name = "CENTER_ID")
    private String center_id;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "THEMENAME")
    private String themename;

    @Column(name = "BRANCH")
    private String branch;

    @Column(name = "KIND")
    private String kind;

    @Column(name = "CURRENCYTYPE")
    private String currencytype;

    @Column(name = "AMOUNT4")
    private String amount4;

    @Column(name = "AMOUNT5")
    private String amount5;
    @Column(name = "AMOUNT6")
    private String amount6;
    @Column(name = "AMOUNT7")
    private String amount7;
    @Column(name = "AMOUNT8")
    private String amount8;
    @Column(name = "AMOUNT9")
    private String amount9;
    @Column(name = "AMOUNT10")
    private String amount10;
    @Column(name = "AMOUNT11")
    private String amount11;
    @Column(name = "AMOUNT12")
    private String amount12;
    @Column(name = "AMOUNT1")
    private String amount1;
    @Column(name = "AMOUNT2")
    private String amount2;
    @Column(name = "AMOUNT3")
    private String amount3;
    @Column(name = "PLANAMOUNT4")
    private String planamount4;
    @Column(name = "PLANAMOUNT5")
    private String planamount5;
    @Column(name = "PLANAMOUNT6")
    private String planamount6;
    @Column(name = "PLANAMOUNT7")
    private String planamount7;
    @Column(name = "PLANAMOUNT8")
    private String planamount8;
    @Column(name = "PLANAMOUNT9")
    private String planamount9;
    @Column(name = "PLANAMOUNT10")
    private String planamount10;
    @Column(name = "PLANAMOUNT11")
    private String planamount11;
    @Column(name = "PLANAMOUNT12")
    private String planamount12;
    @Column(name = "PLANAMOUNT1")
    private String planamount1;
    @Column(name = "PLANAMOUNT2")
    private String planamount2;
    @Column(name = "PLANAMOUNT3")
    private String planamount3;

    @Column(name = "OTHERONE")
    private String otherone;


    @Column(name = "OTHERTWO")
    private String othertwo;


    @Column(name = "OTHERTHREE")
    private String otherthree;


}
