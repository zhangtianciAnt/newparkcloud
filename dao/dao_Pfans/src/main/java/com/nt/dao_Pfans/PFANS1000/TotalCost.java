package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "totalcost")
public class TotalCost extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 费用合计
     */
    @Id
    @Column(name = "TOTALCOST_ID")
    private String totalcost_id;

    /**
     * id
     */
    @Column(name = "PUBLICEXPENSEID")
    private String publicexpenseid;

    /**
     * 发票编号
     */
    @Column(name = "INVOICENUMBER")
    private String invoicenumber;
    /**
     * 发票日期
     */
    @Column(name = "INVOICEDATE")
    private Date invoicedate;
    /**
     * 行号
     */
    @Column(name = "NUMBER")
    private Integer number;
    /**
     * 条件日期
     */
    @Column(name = "CONDITIONDATE")
    private Date conditiondate;
    /**
     * 供应商编码
     */
    @Column(name = "VENDORCODE")
    private String vendorcode;
    /**
     * 币种
     */
    @Column(name = "CURRENCY")
    private String currency;
    /**
     * 发票金额
     */
    @Column(name = "INVOICEAMOUNT")
    private String invoiceamount;
    /**
     * 行金额
     */
    @Column(name = "LINEAMOUNT")
    private String lineamount;
    /**
     * 部门段
     */
    @Column(name = "BUDGETCODING")
    private String budgetcoding;
    /**
     * 科目段
     */
    @Column(name = "SUBJECTNUMBER")
    private String subjectnumber;
    /**
     * 发票说明
     */
    @Column(name = "REMARK")
    private String remark;
    /**
     * 汇率
     */
    @Column(name = "EXCHANGERATE")
    private String exchangerate;
}
