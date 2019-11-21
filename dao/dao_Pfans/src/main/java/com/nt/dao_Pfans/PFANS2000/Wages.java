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
@Table(name = "wages")
public class Wages extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "WAGES_ID")
    private String wages_id;

    @Column(name = "GIVING_ID")
    private String giving_id;

    @Column(name = "DEPARTMENT_ID")
    private String department_id;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "WORKDATE")
    private Date workdate;

    @Column(name = "SEX")
    private String sex;

    @Column(name = "ONLYCHILD")
    private String onlychild;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "BONUS")
    private String bonus;

    @Column(name = "SOCIOLOGY")
    private String sociology;

    @Column(name = "REGISTERED")
    private String registered;

    @Column(name = "PENSION")
    private String pension;

    @Column(name = "MEDICAL")
    private String medical;

    @Column(name = "ACCUMULATION")
    private String accumulation;

    @Column(name = "LASTMONTH")
    private String lastmonth;

    @Column(name = "THISMONTH")
    private String thismonth;

    @Column(name = "BIRTHREST")
    private String birthrest;

    @Column(name = "THISMONTHBASIC")
    private String thismonthbasic;

    @Column(name = "SHORTILLNESS")
    private String shortillness;

    @Column(name = "LONGILLNESS")
    private String longillness;

    @Column(name = "OWEDILIGENCE")
    private String owediligence;

    @Column(name = "OWINGCONTROL")
    private String owingcontrol;

    @Column(name = "ACTUALAMOUNT")
    private String actualamount;

    @Column(name = "SUPPLEMENT")
    private String supplement;

    @Column(name = "TELEPHONESUBSIDY")
    private String telephonesubsidy;

    @Column(name = "HOUSINGSUBSIDY")
    private String housingsubsidy;

    @Column(name = "LUNCHSUBSIDY")
    private String lunchsubsidy;

    @Column(name = "OVERTIMESUBSIDY")
    private String overtimesubsidy;

    @Column(name = "OTHER1")
    private String other1;

    @Column(name = "TOTAL1")
    private String total1;

    @Column(name = "TRAFFIC")
    private String traffic;

    @Column(name = "WASHINGTHEORY")
    private String washingtheory;

    @Column(name = "OTHER2")
    private String other2;

    @Column(name = "APPRECIATION")
    private String appreciation;

    @Column(name = "OTHER3")
    private String other3;

    @Column(name = "TOTAL2")
    private String total2;

    @Column(name = "TAXESTOTAL")
    private String taxestotal;

    @Column(name = "HEATING")
    private String heating;

    @Column(name = "ONLYCHILDMONEY")
    private String onlychildmoney;

    @Column(name = "TOTAL3")
    private String total3;

    @Column(name = "TOTALWAGES")
    private String totalwages;

    @Column(name = "ENDOWMENTINSURANCE")
    private String endowmentinsurance;

    @Column(name = "MEDICALINSURANCE")
    private String medicalinsurance;

    @Column(name = "UNEMPLOYMENTINSURANCE")
    private String unemploymentinsurance;

    @Column(name = "SOCIALINSURANCE")
    private String socialinsurance;

    @Column(name = "ADJUSTMENT")
    private String adjustment;

    @Column(name = "ACCUMULATIONFUND")
    private String accumulationfund;

    @Column(name = "DISCIPLINARYCONTROL")
    private String disciplinarycontrol;

    @Column(name = "THISMONTHTERM")
    private String thismonthterm;

    @Column(name = "THISMONTHADDITIONAL")
    private String thismonthadditional;

    @Column(name = "THISMONTHDUTYFREE")
    private String thismonthdutyfree;

    @Column(name = "LASTDUTYFREE")
    private String lastdutyfree;

    @Column(name = "HOUSINGMONEYS")
    private String housingmoneys;

    @Column(name = "OTHER4")
    private String other4;

    @Column(name = "OTHER5")
    private String other5;

    @Column(name = "SHOULDWAGES")
    private String shouldwages;

    @Column(name = "SHOULDCUMULATIVE")
    private String shouldcumulative;

    @Column(name = "SHOULDPAYTAXES")
    private String shouldpaytaxes;

    @Column(name = "THISMONTHADJUSTMENT")
    private String thismonthadjustment;

    @Column(name = "THISADJUSTMENT")
    private String thisadjustment;

    @Column(name = "REALWAGES")
    private String realwages;

    @Column(name = "COMENDOWMENTINSURANCE")
    private String comendowmentinsurance;

    @Column(name = "COMMEDICALINSURANCE")
    private String commedicalinsurance;

    @Column(name = "COMUNEMPLOYMENTINSURANCE")
    private String comunemploymentinsurance;

    @Column(name = "COMINJURYINSURANCE")
    private String cominjuryinsurance;

    @Column(name = "COMBIRTHINSURANCE")
    private String combirthinsurance;

    @Column(name = "COMHEATING")
    private String comheating;

    @Column(name = "COMACCUMULATIONFUND")
    private String comaccumulationfund;

    @Column(name = "TOTAL")
    private String total;

    @Column(name = "TOTALADJUSTMENT")
    private String totaladjustment;

    @Column(name = "LABOURUNIONBASE")
    private String labourunionbase;

    @Column(name = "LABOURUNIONFUNDS")
    private String labourunionfunds;

    @Column(name = "COMTOTALWAGES")
    private String comtotalwages;

    @Column(name = "BONUSMONEY")
    private String bonusmoney;

    @Column(name = "TOTALBONUS")
    private String totalbonus;

    @Column(name = "OTHER6")
    private String other6;

    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
