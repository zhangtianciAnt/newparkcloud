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
@Table(name = "base")
public class Base extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 基数ID
     */
    @Id
    @Column(name = "BASE_ID")
    private String base_id;

    /**
     * 給与計算ID
     */
    @Column(name = "GIVING_ID")
    private String giving_id;

    /**
     * 部门
     */
    @Column(name = "DEPARTMENT_ID")
    private String department_id;

    /**
     * 名字
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * Rn
     */
    @Column(name = "RN")
    private String rn;

    /**
     * 性別
     */
    @Column(name = "SEX")
    private String sex;

    /**
     * 独生子女
     */
    @Column(name = "ONLYCHILD")
    private String onlychild;

    /**
     * 入/退職/産休
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 奨金計上
     */
    @Column(name = "BONUS")
    private String bonus;

    /**
     * 1999年前社会人
     */
    @Column(name = "SOCIOLOGY")
    private String sociology;

    /**
     * 大連戸籍
     */
    @Column(name = "REGISTERED")
    private String registered;

    /**
     * 上月月工资
     */
    @Column(name = "LASTMONTH")
    private String lastmonth;

    /**
     * 上月基本工资
     */
    @Column(name = "LASTMONTHBASIC")
    private String lastmonthbasic;

    /**
     * 上月职责工资
     */
    @Column(name = "LASTMONTHDUTY")
    private String lastmonthduty;

    /**
     * 本月月工资
     */
    @Column(name = "THISMONTH")
    private String thismonth;

    /**
     * 当月基本工资
     */
    @Column(name = "THISMONTHBASIC")
    private String thismonthbasic;

    /**
     * 当月职责工资
     */
    @Column(name = "THISMONTHDUTY")
    private String thismonthduty;

    /**
     * 3月前基本工资
     */
    @Column(name = "TMABASIC")
    private String tmabasic;

    /**
     * 養老・失業・工傷基数
     */
    @Column(name = "PENSION")
    private String pension;

    /**
     * 医療・生育基数
     */
    @Column(name = "MEDICAL")
    private String medical;

    /**
     * 公积金基数
     */
    @Column(name = "ACCUMULATION")
    private String accumulation;

    /**
     * 采暖费
     */
    @Column(name = "HEATING")
    private String heating;

    /**
     * 入社日
     */
    @Column(name = "WORKDATE")
    private String workdate;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;

    /**
     * 工号
     */
    @Column(name = "JOBNUMBER")
    private String jobnumber;

    /**
     * GROUP ID
     */
    @Column(name = "GROUPID")
    private String groupid;

    /**
     * 上月试用天数
     */
    @Column(name = "LASTMONTHSUITDAYS")
    private String lastmonthsuitdays;

    /**
     * 上月正式天数
     */
    @Column(name = "LASTMONTHDAYS")
    private String lastmonthdays;

    /**
     * 本月试用天数
     */
    @Column(name = "THISMONTHSUITDAYS")
    private String thismonthsuitdays;

    /**
     * 本月正式天数
     */
    @Column(name = "THISMONTHDAYS")
    private String thismonthdays;

    /**
     * 试用期截止日
     */
    @Column(name = "ENDDATE")
    private String enddate;

    /**
     * RN基本工资
     */
    @Column(name = "RNBASESALARY")
    private String rnbasesalary;
}
