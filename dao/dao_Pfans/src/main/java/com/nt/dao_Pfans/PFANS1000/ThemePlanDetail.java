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
@Table(name = "themeplandetail")
public class ThemePlanDetail extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "THEMEPLANDETAIL_ID")
    private String themeplandetail_id;

    @Column(name = "THEMEPLAN_ID")
    private String themeplan_id;

    @Column(name = "PTHEMEPLANDETAIL_ID")
    private String pthemeplandetail_id;

    @Column(name = "CENTER_ID")
    private String center_id;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "MONTH")
    private String month;

    @Column(name = "THEME_ID")
    private String theme_id;

    @Column(name = "THEMENAME")
    private String themename;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "BRANCH")
    private String branch;

    @Column(name = "KIND")
    private String kind;

    @Column(name = "CONTRACTTYPE")
    private String contracttype;

    @Column(name = "CURRENCYTYPE")
    private String currencytype;

    @Column(name = "ASSIGNOR")
    private String assignor;

    @Column(name = "PERSONNEL191")
    private String personnel191;

    @Column(name = "AMOUNT191")
    private String amount191;

    @Column(name = "PERSONNEL192")
    private String personnel192;

    @Column(name = "AMOUNT192")
    private String amount192;

    @Column(name = "PERSONNEL193")
    private String personnel193;

    @Column(name = "AMOUNT193")
    private String amount193;

    @Column(name = "PERSONNEL4")
    private String personnel4;

    @Column(name = "AMOUNT4")
    private String amount4;

    @Column(name = "PERSONNEL5")
    private String personnel5;

    @Column(name = "AMOUNT5")
    private String amount5;

    @Column(name = "PERSONNEL6")
    private String personnel6;

    @Column(name = "AMOUNT6")
    private String amount6;

    @Column(name = "PERSONNEL7")
    private String personnel7;

    @Column(name = "AMOUNT7")
    private String amount7;

    @Column(name = "PERSONNEL8")
    private String personnel8;

    @Column(name = "AMOUNT8")
    private String amount8;

    @Column(name = "PERSONNEL9")
    private String personnel9;

    @Column(name = "AMOUNT9")
    private String amount9;

    @Column(name = "PERSONNEL10")
    private String personnel10;

    @Column(name = "AMOUNT10")
    private String amount10;

    @Column(name = "PERSONNEL11")
    private String personnel11;

    @Column(name = "AMOUNT11")
    private String amount11;

    @Column(name = "PERSONNEL12")
    private String personnel12;

    @Column(name = "AMOUNT12")
    private String amount12;

    @Column(name = "PERSONNEL1")
    private String personnel1;

    @Column(name = "AMOUNT1")
    private String amount1;

    @Column(name = "PERSONNEL2")
    private String personnel2;

    @Column(name = "AMOUNT2")
    private String amount2;

    @Column(name = "PERSONNEL3")
    private String personnel3;

    @Column(name = "AMOUNT3")
    private String amount3;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "ROWINDEX")
    private String rowindex;
}
