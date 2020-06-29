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
public class SalesExportVo {

//    /**
//     * 走货
//     */
//    private TransportGood transportgood;
//
//    /**
//     * 走货-销售明细子表
//     */
//    private Saledetails saledetails;
//
//    /**
//     * 走货-用款申请记录子表
//     */
//    private Applicationrecord applicationrecord;

    //    合同号
    private String contractnumber;

    //产品英文名
    private String productus;

    //规格
    private String specifications;

    //客户名称
    private String customer;

    //联系人
    private String linkmanname;

    //国家
    private String country;

    //首单时间
    private Date firstordertime;

    //销售单价
    private String unitprice;

    //    单价单位
    private String unit;

    //    币种
    private String currency;

    //    数量
    private String numbers;

    //    销售金额
    private String salesamount;

    // 发起回款确认
    private String paymentstatus;

    //    贸易术语
    private String tradeterms;

    //    收款账户
    private String collectionaccount;

    //    销售合同回签
    private String signback;

    //    销售担当
    private String saleresponsibility;

    //    付款方式
    private String payment;

    //    订单要求
    private String orders;

    //    数据字典
    private String value1;

}
