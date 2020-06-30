package com.nt.dao_AOCHUAN.AOCHUAN5000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import jxl.write.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinReport {

//    //季度
//    @Column(name = "BILLTIME")
//    private String billtime;
//
//    月份
//    @Column(name = "BILLTIME")
//    private String billtime;

    //合同号
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    //公司
//    @Column(name = "CONTRACTNUMBER")
//    private String contractnumber;

    //产品名
    @Column(name = "CHINANAME")
    private String chinaname;
    //客户名称
    @Column(name = "CUSTOMERNAMECN")
    private String customernamecn;
    //国家
    @Column(name = "COUNTRY")
    private String country;
    //供应商名称
    @Column(name = "SUPPLIERNAMECN")
    private String suppliernamecn;
    //数量
    @Column(name = "NUMBERS")
    private String numbers;
    //币种
    @Column(name = "CURRENCY")
    private String currency;
    //汇率
    @Column(name = "EX_RATE")
    private String ex_rate;
    //销售单价
    @Column(name = "UNITPRICE")
    private String unitprice;
    //销售金额
    @Column(name = "SALESAMOUNT")
    private String salesamount;
    //到账金额
    @Column(name = "AMOUNTRECEIVED")
    private String amountreceived;
    //付款方式
    @Column(name = "PAYMENT")
    private String payment;
    //收款账户
    @Column(name = "COLLECTIONACCOUNT")
    private String collectionaccount;
    //销售担当
    @Column(name = "saleresponsibility")
    private String saleresponsibility;
    //销售部门
    @Column(name = "SALESDEPARTMENT")
    private String salesdepartment;
    //采购币种
    @Column(name = "CURRENCY1")
    private String currency1;
    //采购单价
    @Column(name = "UNITPRICE1")
    private String unitprice1;
    //采购金额
    @Column(name = "PURCHASEAMOUNT")
    private String purchaseamount;
    //采购人民币金额
    @Column(name = "PURCHASEAMOUNTRMB")
    private String purchaseamountrmb;
    //海关编码
    @Column(name = "HSCODE")
    private String hscode;
    //退税率
    @Column(name = "DRAWBACK")
    private String drawback;
    //退税额
    @Column(name = "TAXREFUNDAMOUNT")
    private String taxrefundamount;
    //采购担当
    @Column(name = "PRODUCTRESPONSIBILITY")
    private String productresponsibility;
    //鉴定费用
    @Column(name = "APPRAISALCOST")
    private String appraisalcost;
//    //运输方式
//    @Column(name = "APPRAISALCOST")
//    private String appraisalcost;
    //报关情况
    @Column(name = "BILL")
    private String bill;
    //提单时间
    @Column(name = "BILLTIME")
    private Date billtime;
    //运费
    @Column(name = "FREIGHT")
    private String freight;
    //保费
    @Column(name = "PREMIUM")
    private String premium;
//    //收款时间
//    @Column(name = "COLLECTIONDATE")
//    private Date collectiondate;
//    //付款时间
//    @Column(name = "PAYMENTTIME")
//    private Date paymenttime;
//    //付款金额
//    @Column(name = "PAYMENTAMOUNT")
//    private String paymentamount;
//    //开票金额
//    @Column(name = "INVOICEAMOUNT")
//    private String invoiceamount;
//    //采购发票号码
//    @Column(name = "PURCHASEINVOICENO")
//    private String purchaseinvoiceno;
//    //开票日期
//    @Column(name = "INVOICEDATE")
//    private Date invoicedate;
//    //佣金
    @Column(name = "COMMISSIONAMOUNTA")
    private String commissionamounta;
    //利润
    @Column(name = "PROFIT")
    private String profit;

//    //退货
//    @Column(name = "RETURNGOODS")
//    private String returngoods;

}
