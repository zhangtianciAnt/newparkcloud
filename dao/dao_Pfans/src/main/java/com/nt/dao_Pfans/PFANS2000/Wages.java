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
    @Column(name = "wages_ID")
    private String wages_id;

    @Column(name = "giving_id")
    private String giving_id;

    @Column(name = "department_id")
    private String department_id;

    @Column(name = "user_id")
    private String user_id;

    @Column(name = "workString")
    private Date workString;

    @Column(name = "sex")
    private String sex;

    @Column(name = "onlychild")
    private String onlychild;

    @Column(name = "type")
    private String type;

    @Column(name = "bonus")
    private String bonus;

    @Column(name = "sociology")
    private String sociology;

    @Column(name = "registered")
    private String registered;

    @Column(name = "pension")
    private String pension;

    @Column(name = "medical")
    private String medical;

    @Column(name = "accumulation")
    private String accumulation;

    @Column(name = "lastmonth")
    private String lastmonth;

    @Column(name = "thismonth")
    private String thismonth;

    @Column(name = "birthrest")
    private String birthrest;

    @Column(name = "thismonthbasic")
    private String thismonthbasic;

    @Column(name = "shortillness")
    private String shortillness;

    @Column(name = "longillness")
    private String longillness;

    @Column(name = "owediligence")
    private String owediligence;

    @Column(name = "owingcontrol")
    private String owingcontrol;

    @Column(name = "actualamount")
    private String actualamount;

    @Column(name = "supplement")
    private String supplement;

    @Column(name = "telephonesubsidy")
    private String telephonesubsidy;

    @Column(name = "housingsubsidy")
    private String housingsubsidy;

    @Column(name = "lunchsubsidy")
    private String lunchsubsidy;

    @Column(name = "overtimesubsidy")
    private String overtimesubsidy;

    @Column(name = "other1")
    private String other1;

    @Column(name = "total1")
    private String total1;

    @Column(name = "traffic")
    private String traffic;

    @Column(name = "washingtheory")
    private String washingtheory;

    @Column(name = "other2")
    private String other2;

    @Column(name = "appreciation")
    private String appreciation;

    @Column(name = "other3")
    private String other3;

    @Column(name = "total2")
    private String total2;

    @Column(name = "taxestotal")
    private String taxestotal;

    @Column(name = "heating")
    private String heating;

    @Column(name = "onlychildmoney")
    private String onlychildmoney;

    @Column(name = "total3")
    private String total3;

    @Column(name = "totalwages")
    private String totalwages;

    @Column(name = "endowmentinsurance")
    private String endowmentinsurance;

    @Column(name = "medicalinsurance")
    private String medicalinsurance;

    @Column(name = "unemploymentinsurance")
    private String unemploymentinsurance;

    @Column(name = "socialinsurance")
    private String socialinsurance;

    @Column(name = "adjustment")
    private String adjustment;

    @Column(name = "accumulationfund")
    private String accumulationfund;

    @Column(name = "disciplinarycontrol")
    private String disciplinarycontrol;

    @Column(name = "thismonthterm")
    private String thismonthterm;

    @Column(name = "thismonthadditional")
    private String thismonthadditional;

    @Column(name = "thismonthdutyfree")
    private String thismonthdutyfree;

    @Column(name = "lastdutyfree")
    private String lastdutyfree;

    @Column(name = "housingmoneys")
    private String housingmoneys;

    @Column(name = "other4")
    private String other4;

    @Column(name = "other5")
    private String other5;

    @Column(name = "shouldwages")
    private String shouldwages;

    @Column(name = "shouldcumulative")
    private String shouldcumulative;

    @Column(name = "shouldpaytaxes")
    private String shouldpaytaxes;

    @Column(name = "thismonthadjustment")
    private String thismonthadjustment;

    @Column(name = "thisadjustment")
    private String thisadjustment;

    @Column(name = "realwages")
    private String realwages;

    @Column(name = "comendowmentinsurance")
    private String comendowmentinsurance;

    @Column(name = "commedicalinsurance")
    private String commedicalinsurance;

    @Column(name = "comunemploymentinsurance")
    private String comunemploymentinsurance;

    @Column(name = "cominjuryinsurance")
    private String cominjuryinsurance;

    @Column(name = "combirthinsurance")
    private String combirthinsurance;

    @Column(name = "comheating")
    private String comheating;

    @Column(name = "comaccumulationfund")
    private String comaccumulationfund;

    @Column(name = "total")
    private String total;

    @Column(name = "totaladjustment")
    private String totaladjustment;

    @Column(name = "labourunionbase")
    private String labourunionbase;

    @Column(name = "labourunionfunds")
    private String labourunionfunds;

    @Column(name = "comtotalwages")
    private String comtotalwages;

    @Column(name = "bonusmoney")
    private String bonusmoney;

    @Column(name = "totalbonus")
    private String totalbonus;

    @Column(name = "other6")
    private String other6;

    @Column(name = "rowindex")
    private Integer rowindex;
}
