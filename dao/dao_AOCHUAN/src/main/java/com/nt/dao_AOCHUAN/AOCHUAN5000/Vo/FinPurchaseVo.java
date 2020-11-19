package com.nt.dao_AOCHUAN.AOCHUAN5000.Vo;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinPurchaseVo extends BaseModel {

    private String purchase_id;
    //合同号
    private String contractnumber;
    //产品中文名
    private String producten;
    //供应商名称
    private String supplier;
    //采购单价
    private String unitprice1;
    //币种
    private String currency1;
    //采购金额
    private String purchaseamount;
    //采购合同回签
    private String signback1;
    //海关编码
    private String hscode;
    //退税率
    private String drawback;
    //危险品
    private String dangerous;
    //计划发货时间
    private Date deliverytime;
    //预计付款时间
    private Date paymenttime;
    //实际付款时间
    private Date ap_date;
    //唛头
    private String marks;
    //发货状态
    private String deliverystatus;
    //发票号码
    private String invoicenumber;
    //付款状态
    private String paymentstatus;
    //采购担当
    private String productresponsibility;
    //凭证状态
    private String credential_status;
    //附件
    private  String attachment;
    //走货主键
    private  String transportgood_id;
    //付款凭证号
    private  String credential_pay;
    //发票凭证号
    private  String credential_invoice;
    //汇率
    private  String ex_rate;
    //数量
    private  String purchase_amount;
    //单位
    private String unit1;
    //总金额（应付金额）
    private String summonery;

    private String sumamount;

    private String paymentaccount;

    private String realpay;

    private String realamount;

    private String applicationrecord_id;

    //付款时间
    private Date fktime;

    //付款金额
    private String fkmoney;

    private String amountreceived;

    private String invoiceamount;

    private String billingtime;

    private String commissionamounta;
    //供应商名字
    private String suppliercn;
    //银行信息
    private String bankinformation;

}
