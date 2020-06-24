package com.nt.dao_AOCHUAN.AOCHUAN3000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN3000.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportVo {

    /**
     * 走货
     */
    private TransportGood transportgood;

    /**
     * 走货-销售明细子表
     */
    private Saledetails saledetails;

    /**
     * 走货-用款申请记录子表
     */
    private Applicationrecord applicationrecord;

    private String transportgood_id;

    private String contractnumber;

    private String productus;

    private String specifications;

    private String paybackstatus;

    private String customer;

    private String account;

    private String country;

    private String unitprice;

    private String currency;

    private String amount;

    private String purchase_amount;

    private String salesamount;

    private String payment;

    private String tradeterms;

    private String collectionaccount;

    private String signback;

    private String saleresponsibility;

    private Date arrivaltime;

    private String orders;

    private String producten;

    private String supplier;

    private String unitprice1;

    private String currency1;

    private String purchaseamount;

    private String signback1;

    private String hscode;

    private String drawback;

    private String dangerous;

    private Date deliverytime;

    private Date paymenttime;

    private String marks;

    private String deliverystatus;

    private String invoicenumber;

    private String paymentstatus;

    private String productresponsibility;

    private Date bookingtime;

    private String departure;

    private String forwardingcompany;

    private String appraisalcost;

    private String entrynumber;

    private Date interceptiontime;

    private Date cutofftime;

    private String warehouse;

    private String bill;

    private Date billtime;

    private String freight;

    private String premium;

    private String billresponsibility;

    private String enclosure1;

    private String enclosure2;

    private String enclosure3;

    private String productsid;

    private String supplierid;

    private String customerid;

    private Date firstordertime;

    private String firstorderduration;

    private String ordertype;

    private String receptgoods;

    private Integer type;

    private Date ap_date;

    private String requestpayment;

    private String returnpayment;

    private String msds;

    private String casnum;

    @Transient
    private boolean notice;

    @Transient
    private Integer finance = 0;

}
