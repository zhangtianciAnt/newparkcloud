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
     * 4月实际
     */
    @Column(name = "MONEYACTUAL4")
    private Double moneyactual4;
    /**
     * 5月实际
     */
    @Column(name = "MONEYACTUAL5")
    private Double moneyactual5;
    /**
     * 6月实际
     */
    @Column(name = "MONEYACTUAL6")
    private Double moneyactual6;
    /**
     * 第一季度
     */
    @Column(name = "TOTALACTUAL1Q")
    private Double totalactual1q;
    /**
     * 7月实际
     */
    @Column(name = "MONEYACTUAL7")
    private Double moneyactual7;
    /**
     * 8月实际
     */
    @Column(name = "MONEYACTUAL8")
    private Double moneyactual8;
    /**
     * 9月实际
     */
    @Column(name = "MONEYACTUAL9")
    private Double moneyactual9;
    /**
     * 第二季度
     */
    @Column(name = "TOTALACTUAL2Q")
    private Double totalactual2q;

    /**
     * 10月实际
     */
    @Column(name = "MONEYACTUAL10")
    private Double moneyactual10;
    /**
     * 11月实际
     */
    @Column(name = "MONEYACTUAL11")
    private Double moneyactual11;
    /**
     * 12月实际
     */
    @Column(name = "MONEYACTUAL12")
    private Double moneyactual12;
    /**
     * 第三季度
     */
    @Column(name = "TOTALACTUAL3Q")
    private Double totalactual3q;
    /**
     * 1月实际
     */
    @Column(name = "MONEYACTUAL1")
    private Double moneyactual1;
    /**
     * 2月实际
     */
    @Column(name = "MONEYACTUAL2")
    private Double moneyactual2;
    /**
     * 3月实际
     */
    @Column(name = "MONEYACTUAL3")
    private Double moneyactual3;

    /**
     * 第四季度
     */
    @Column(name = "TOTALACTUAL4Q")
    private Double totalactual4q;
    /**
     * 全年实际合计
     */
    @Column(name = "TOTALACTUAL")
    private Double totalactual;
}
