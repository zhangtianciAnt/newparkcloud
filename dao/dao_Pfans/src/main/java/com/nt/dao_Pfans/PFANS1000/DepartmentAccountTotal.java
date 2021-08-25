package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "departmentaccounttotal")
public class DepartmentAccountTotal extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 部门收支合计ID
     */
    @Id
    @Column(name = "DEPARTMENTACCOUNTTOTAL_ID")
    private String departmentaccounttotalid;
    /**
     * themeID
     */
    @Column(name = "THEME_ID")
    private String theme_id;

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
     * 序号
     */
    @Column(name = "INDEXTYPE")
    private String indextype;

    /**
     * 合同后金额
     */
    @Column(name = "AMOUNT")
    private String amount;
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
     * 10月计划
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
     * 1月计划
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
}
