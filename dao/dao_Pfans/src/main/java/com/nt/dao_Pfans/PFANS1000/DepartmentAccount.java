package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "departmentaccount")
public class DepartmentAccount  extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 部门收支ID
     */
    @Id
    @Column(name = "DEPARTMENTACCOUNT_ID")
    private String departmentaccountid;
    /**
     * themeID
     */
    @Column(name = "THEME_ID")
    private String theme_id;
    /**
     * theme名
     */
    @Column(name = "THEMENAME")
    private String themename;
    /**
     * 委托元类型
     */
    @Column(name = "CONTRACT")
    private String contract;
    /**
     * 委托元
     */
    @Column(name = "TOOLSORGS")
    private String toolsorgs;
    /**
     * 合同ID
     */
    @Column(name = "CONTRACTAPPLICATION_ID")
    private String contractapplication_id;
    /**
     * 合同番号
     */
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    /**
     * 合同金额
     */
    @Column(name = "CLAIMAMOUNTTOTAL")
    private String claimamounttotal;
    /**
     * 合同后金额
     */
    @Column(name = "AMOUNT")
    private String amount;
    /**
     * 契約進捗状況
     */
    @Column(name = "ENTRYCONDITION")
    private String entrycondition;
    /**
     * 合同状态
     */
    @Column(name = "STATE")
    private String state;
    /**
     * 项目ID
     */
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;
    /**
     * 项目编号
     */
    @Column(name = "NUMBERS")
    private String numbers;
    /**
     * 年度
     */
    @Column(name = "YEARS")
    private String years;
    /**
     * 部门
     */
    @Column(name = "DEPARTMENT")
    private String department;
    /**
     * 4月计划
     */
    @Column(name = "MONEYPLAN4")
    private BigDecimal moneyplan4;
    /**
     * 4月实际
     */
    @Column(name = "MONEYACTUAL4")
    private BigDecimal moneyactual4;
    /**
     * 5月计划
     */
    @Column(name = "MONEYPLAN5")
    private BigDecimal moneyplan5;
    /**
     * 5月实际
     */
    @Column(name = "MONEYACTUAL5")
    private BigDecimal moneyactual5;
    /**
     * 6月计划
     */
    @Column(name = "MONEYPLAN6")
    private BigDecimal moneyplan6;
    /**
     * 6月实际
     */
    @Column(name = "MONEYACTUAL6")
    private BigDecimal moneyactual6;

    /**
     * 第一季度
     */
    @Column(name = "TOTALACTUAL1Q")
    private BigDecimal totalactual1q;

    /**
     * 7月计划
     */
    @Column(name = "MONEYPLAN7")
    private BigDecimal moneyplan7;
    /**
     * 7月实际
     */
    @Column(name = "MONEYACTUAL7")
    private BigDecimal moneyactual7;
    /**
     * 8月计划
     */
    @Column(name = "MONEYPLAN8")
    private BigDecimal moneyplan8;
    /**
     * 8月实际
     */
    @Column(name = "MONEYACTUAL8")
    private BigDecimal moneyactual8;
    /**
     * 9月计划
     */
    @Column(name = "MONEYPLAN9")
    private BigDecimal moneyplan9;
    /**
     * 9月实际
     */
    @Column(name = "MONEYACTUAL9")
    private BigDecimal moneyactual9;

    /**
     * 第二季度
     */
    @Column(name = "TOTALACTUAL2Q")
    private BigDecimal totalactual2q;

    /**
    10月计划
     */
    @Column(name = "MONEYPLAN10")
    private BigDecimal moneyplan10;
    /**
     * 10月实际
     */
    @Column(name = "MONEYACTUAL10")
    private BigDecimal moneyactual10;
    /**
     * 11月计划
     */
    @Column(name = "MONEYPLAN11")
    private BigDecimal moneyplan11;
    /**
     * 11月实际
     */
    @Column(name = "MONEYACTUAL11")
    private BigDecimal moneyactual11;
    /**
     * 12月计划
     */
    @Column(name = "MONEYPLAN12")
    private BigDecimal moneyplan12;
    /**
     * 12月实际
     */
    @Column(name = "MONEYACTUAL12")
    private BigDecimal moneyactual12;
    /**
     * 第三季度
     */
    @Column(name = "TOTALACTUAL3Q")
    private BigDecimal totalactual3q;
    /**
     1月计划
     */
    @Column(name = "MONEYPLAN1")
    private BigDecimal moneyplan1;
    /**
     * 1月实际
     */
    @Column(name = "MONEYACTUAL1")
    private BigDecimal moneyactual1;
    /**
     * 2月计划
     */
    @Column(name = "MONEYPLAN2")
    private BigDecimal moneyplan2;
    /**
     * 2月实际
     */
    @Column(name = "MONEYACTUAL2")
    private BigDecimal moneyactual2;
    /**
     * 3月计划
     */
    @Column(name = "MONEYPLAN3")
    private BigDecimal moneyplan3;
    /**
     * 3月实际
     */
    @Column(name = "MONEYACTUAL3")
    private BigDecimal moneyactual3;
    /**
     * 第四季度
     */
    @Column(name = "TOTALACTUAL4Q")
    private BigDecimal totalactual4q;

    /**
     * 全年实际合计
     */
    @Column(name = "TOTALACTUAL")
    private BigDecimal totalactual;


    @Transient
    private String indextype;
}
