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
@Table(name = "fin_sales")
public class FinSales extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //主键
    @Id
    @Column(name = "SALES_ID")
    private String sales_id;
    //合同号
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;
    //产品中文
    @Column(name = "PRODUCTUS")
    private String productus;
    //规格
    @Column(name = "SPECIFICATIONS")
    private String specifications;
    //客户名称
    @Column(name = "CUSTOMER")
    private String customer;
    //客户联系人
    @Column(name = "ACCOUNT")
    private String account;
    //国家
    @Column(name = "COUNTRY")
    private String country;
    //销售单价
    @Column(name = "UNITPRICE")
    private String unitprice;
    //币种
    @Column(name = "CURRENCY")
    private String currency;
    //数量
    @Column(name = "AMOUNT")
    private String amount;
    //销售金额
    @Column(name = "SALESAMOUNT")
    private String salesamount;
    //付款方式
    @Column(name = "PAYMENT")
    private String payment;
    //贸易术语
    @Column(name = "TRADETERMS")
    private String tradeterms;
    //收款账户
    @Column(name = "COLLECTIONACCOUNT")
    private String collectionaccount;
    //销售合同回签
    @Column(name = "SIGNBACK")
    private String signback;
    //销售担当
    @Column(name = "SALERESPONSIBILITY")
    private String saleresponsibility;
    //回款日期
    @Column(name = "ARRIVALTIME")
    private Date arrivaltime;
    //订单需求
    @Column(name = "ORDERS")
    private String orders;
    //回款状态
    @Column(name = "ARRIVAL_STATUS")
    private String arrival_status;
    //凭证状态
    @Column(name = "CREDENTIAL_STATUS")
    private String credential_status;
    //佣金金额
    @Column(name = "COMMISSION_AMOUNT")
    private String commission_amount;
}

