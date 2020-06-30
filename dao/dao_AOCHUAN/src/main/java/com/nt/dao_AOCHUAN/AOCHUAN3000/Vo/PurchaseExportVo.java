package com.nt.dao_AOCHUAN.AOCHUAN3000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseExportVo {

    //    合同号
    private String contractnumber;

    //产品中文名
    private String chinaname;

    //供应商名称
    private String suppliernamecn;

    //采购单价
    private String unitprice1;

    //单价单位（数量）
    private String unit;

    //采购币种
    private String currency1;

    //    数量（新增）
    private String numbers1;

    //    采购金额
    private String purchaseamount;

    //    采购合同回签
    private String signback1;

    //    海关编码
    private String hscode;

    //    退税率
    private String drawback;

    //    危险品
    private String dangerous;

    //    计划发货时间--删除
    private String deliverytime;

    //    预计付款时间（提醒）
    private String paymenttime;

    //    唛头
    private String marks;

    //    MSDS
    private String MSDS;

    //    发货状态
    private String deliverystatus;

    //    发票编号
    private String invoicenumber;

    //    付款状态
    private String paymentstatus;

    //    采购担当
    private String productresponsibility;

    //    付款时间
    private Date realdate;
}
