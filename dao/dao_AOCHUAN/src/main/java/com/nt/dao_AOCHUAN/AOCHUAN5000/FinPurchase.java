package com.nt.dao_AOCHUAN.AOCHUAN5000;

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
@Table(name = "fin_purchase")
public class FinPurchase extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //主键
    @Id
    @Column(name = "PURCHASE_ID")
    private String purchase_id;
    //合同号
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;
    //产品中文名
    @Column(name = "PRODUCTEN")
    private String producten;
    //供应商名称
    @Column(name = "SUPPLIER")
    private String supplier;
    //采购单价
    @Column(name = "UNITPRICE1")
    private String unitprice1;
    //币种
    @Column(name = "CURRENCY1")
    private String currency1;
    //采购金额
    @Column(name = "PURCHASEAMOUNT")
    private String purchaseamount;
    //采购合同回签
    @Column(name = "SIGNBACK1")
    private String signback1;
    //海关编码
    @Column(name = "HSCODE")
    private String hscode;
    //退税率
    @Column(name = "DRAWBACK")
    private String drawback;
    //危险品
    @Column(name = "DANGEROUS")
    private String dangerous;
    //计划发货时间
    @Column(name = "DELIVERYTIME")
    private Date deliverytime;
    //预计付款时间
    @Column(name = "PAYMENTTIME")
    private Date paymenttime;
    //实际付款时间
    @Column(name = "AP_DATE")
    private Date ap_date;
    //唛头
    @Column(name = "MARKS")
    private String marks;
    //发货状态
    @Column(name = "DELIVERYSTATUS")
    private String deliverystatus;
    //发票号码
    @Column(name = "INVOICENUMBER")
    private String invoicenumber;
    //付款状态
    @Column(name = "PAYMENTSTATUS")
    private String paymentstatus;
    //采购担当
    @Column(name = "PRODUCTRESPONSIBILITY")
    private String productresponsibility;
    //凭证状态
    @Column(name = "CREDENTIAL_STATUS")
    private String credential_status;
    //附件
    @Column(name = "ATTACHMENT")
    private  String attachment;
    //走货主键
    @Column(name = "TRANSPORTGOOD_ID")
    private  String transportgood_id;
    //付款凭证号
    @Column(name = "CREDENTIAL_PAY")
    private  String credential_pay;
    //发票凭证号
    @Column(name = "CREDENTIAL_INVOICE")
    private  String credential_invoice;
    //汇率
    @Column(name = "EX_RATE")
    private  String ex_rate;
}
