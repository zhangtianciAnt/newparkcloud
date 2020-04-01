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
    private String wages_id;    // 工资表主键

    @Column(name = "GIVING_ID")
    private String giving_id;   // give表主键

    @Column(name = "DEPARTMENT_ID")
    private String department_id;   //  部门id

    @Column(name = "USER_ID")
    private String user_id;     //  用户id

    @Column(name = "WORKDATE")
    private Date workdate;      // 入社年月

    @Column(name = "SEX")
    private String sex;         //  性別

    @Column(name = "ONLYCHILD")
    private String onlychild;   // 独生子女

    @Column(name = "TYPE")
    private String type;    // 入/退職/産休

    @Column(name = "BONUS")
    private String bonus;   // 奨金計上

    @Column(name = "SOCIOLOGY")
    private String sociology;   // 1999年前社会人

    @Column(name = "REGISTERED")
    private String registered;  // 大連戸籍

    @Column(name = "PENSION")
    private String pension;     // 養老・失業・工傷保険基数

    @Column(name = "MEDICAL")
    private String medical;     // 医療・生育保険基数

    @Column(name = "ACCUMULATION")
    private String accumulation;        // 住房公积金基数

    @Column(name = "LASTMONTH")
    private String lastmonth;       // 上月基本給

    @Column(name = "THISMONTH")
    private String thismonth;       //  本月基本給

    @Column(name = "LASTMONTHBASIC")
    private String lastmonthbasic;  //上个月基本工资
    @Column(name = "LASTMONTHDUTY")
    private String lastmonthduty; //上月职责工资
    @Column(name = "BASETHISMONTHBASIC")
    private String basethismonthbasic;//本月基本工资
    @Column(name = "THISMONTHDUTY")
    private String thismonthduty;//本月职责工资


    @Column(name = "RNBASESALARY")  //  RN基本工资 ***
    private String rnbasesalary;

    @Column(name = "BIRTHREST")
    private String birthrest;       // 産休出勤日数(看護休暇日数)

    @Column(name = "THISMONTHBASICBASIC")
    private String thismonthbasicbasic;//本月入/退職/産休基本工资

    @Column(name = "THISMONTHBASIC")
    private String thismonthbasic;  // 本月入/退職/産休基本給

    @Column(name = "SHORTILLNESS")
    private String shortillness;    // 短病欠時数

    @Column(name = "LONGILLNESS")
    private String longillness;     // 长病欠時数

    @Column(name = "OWEDILIGENCE")
    private String owediligence;    // 欠勤時数

    @Column(name = "OWINGCONTROL")
    private String owingcontrol;    // 病欠/欠勤控除

    @Column(name = "ACTUALAMOUNT")
    private String actualamount;    //  基本給实际金额

    @Column(name = "THISMONTHBASICGEI")
    private String thismonthbasicgei;   //本月基本给

    @Column(name = "THISMONTHDUTYGEI")
    private String thismonthdutygei;    //本月职责给


    @Column(name = "SUPPLEMENT")
    private String supplement;      // 公积金补充(加班补充）不要了

    @Column(name = "TELEPHONESUBSIDY")
    private String telephonesubsidy;    //  电话补助 不要了

    @Column(name = "HOUSINGSUBSIDY")
    private String housingsubsidy;      // 住房补助 不要了

    @Column(name = "LUNCHSUBSIDY")
    private String lunchsubsidy;        // 午餐补助 不要了

    @Column(name = "OVERTIMESUBSIDY")
    private String overtimesubsidy;     // 加班补助

    @Column(name = "OTHER1")
    private String other1;              // 其他1 不要了

    @Column(name = "TOTAL1")
    private String total1;              // 小计1(基本給+补助)

    @Column(name = "YKBZ")                // 一括补助 ***
    private String ykbz;

    public String getYkbz() {
        return ykbz;
    }

    public void setYkbz(String ykbz) {
        this.ykbz = ykbz;
    }

    @Column(name = "TRAFFIC")
    private String traffic;             // 交通补助  不要了

    @Column(name = "WASHINGTHEORY")
    private String washingtheory;       // 女性洗理费  不要了

    @Column(name = "OTHER2")
    private String other2;              // 其他2

    @Column(name = "APPRECIATION")
    private String appreciation;        // 月度賞与

    @Column(name = "OTHER3")
    private String other3;              // 其他3   手动录入

    @Column(name = "TOTAL2")
    private String total2;              // 小计2   要加上手动录入的other3         add

    @Column(name = "TAXESTOTAL")
    private String taxestotal;          // 纳税工资总额(小计1+2)

    @Column(name = "HEATING")
    private String heating;             // 采暖费

    @Column(name = "ONLYCHILDMONEY")
    private String onlychildmoney;      // 独生子女费

    @Column(name = "TOTAL3")
    private String total3;              // 小计3

    @Column(name = "TOTALWAGES")
    private String totalwages;          // 工资总额(纳税+免税)

    @Column(name = "ENDOWMENTINSURANCE")
    private String endowmentinsurance;   // 养老保险

    @Column(name = "MEDICALINSURANCE")
    private String medicalinsurance;    // 医疗保险

    @Column(name = "UNEMPLOYMENTINSURANCE")
    private String unemploymentinsurance;   // 失业保险

    @Column(name = "SOCIALINSURANCE")
    private String socialinsurance;         // 个人社会保险(専項控除)

    @Column(name = "ADJUSTMENT")
    private String adjustment;              // 调整数   手工录入   个人社会保险(専項控除)    edit

    @Column(name = "ACCUMULATIONFUND")
    private String accumulationfund;        // 个人住房公积金(専項控除)

    @Column(name = "DISCIPLINARYCONTROL")
    private String disciplinarycontrol;     // 个人社会保险费+公积金(専項控除)合计

    @Column(name = "THISMONTHTERM")
    private String thismonthterm;           // 専項控除累計（当月まで）

    @Column(name = "THISMONTHADDITIONAL")
    private String thismonthadditional;     // 附加控除累計（当月まで）

    @Column(name = "THISMONTHDUTYFREE")
    private String thismonthdutyfree;       // 免税分累計（当月まで）

    @Column(name = "LASTDUTYFREE")
    private String lastdutyfree;            // 年間累計税金（先月まで）

    @Column(name = "HOUSINGMONEYS")
    private String housingmoneys;           // 住房公积金应纳税金额

    @Column(name = "OTHER4")
    private String other4;                  // 其他4公司负担社保（24元）

    @Column(name = "OTHER5")
    private String other5;                  // 其他5（保険：福祉）

    @Column(name = "SHOULDWAGES")
    private String shouldwages;             // 当月応発工資（工资总额(纳税+免税)+只納税）

    @Column(name = "SHOULDCUMULATIVE")
    private String shouldcumulative;        // 累計応発工資（当月含）

    @Column(name = "SHOULDPAYTAXES")
    private String shouldpaytaxes;          // 累計应纳税所得额

    @Column(name = "THISMONTHADJUSTMENT")
    private String thismonthadjustment;     // 本月应扣缴所得税

    @Column(name = "THISADJUSTMENT")
    private String thisadjustment;          //  调整数     手工录入     本月应扣缴所得税       edit

    @Column(name = "REALWAGES")
    private String realwages;               //  当月实发工资   需要添加其他6字段    add

    @Column(name = "COMENDOWMENTINSURANCE")
    private String comendowmentinsurance;   //  养老保险

    @Column(name = "COMMEDICALINSURANCE")
    private String commedicalinsurance;     //  医疗保险

    @Column(name = "COMUNEMPLOYMENTINSURANCE")
    private String comunemploymentinsurance;    // 失业保险

    @Column(name = "COMINJURYINSURANCE")
    private String cominjuryinsurance;      // 工伤保险

    @Column(name = "COMBIRTHINSURANCE")
    private String combirthinsurance;       // 生育保险

    @Column(name = "COMHEATING")
    private String comheating;              // 暖房费

    @Column(name = "COMACCUMULATIONFUND")
    private String comaccumulationfund;     // 住房公积金

    @Column(name = "TOTAL")
    private String total;                   // 总计

    @Column(name = "TOTALADJUSTMENT")
    private String totaladjustment;         // 调整数      手工调整总计

    @Column(name = "LABOURUNIONBASE")
    private String labourunionbase;         // 工会经费基数

    @Column(name = "LABOURUNIONFUNDS")
    private String labourunionfunds;        // 工会经费

    @Column(name = "COMTOTALWAGES")
    private String comtotalwages;           // 工资总额(纳税+免税)+福祉+公司負担+工会経費总计

    @Column(name = "BONUSMONEY")
    private String bonusmoney;              // 計上奨金

    @Column(name = "NJJY")   //年休结余
    private String njjy;

    @Column(name = "TOTALBONUS")
    private String totalbonus;              // 总计+計上奨金

    @Column(name = "OTHER6")
    private String other6;                  // 其他6      手动录入

    @Column(name = "ROWINDEX")
    private Integer rowindex;               // 顺序
}
