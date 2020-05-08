package com.nt.dao_AOCHUAN.AOCHUAN5000;

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
@Table(name = "acctgrul")
public class AcctgRul extends BaseModel {

    //分录规则id
    @Id
    @Column(name = "ACCTGRUL_ID")
    private String acctgrul_id;
    //外键：凭证信息id
    @Column(name = "CRDLINFO_FID")
    private String crdlinfo_fid;
    //摘要
    @Column(name = "REMARKS")
    private String remarks;
    //科目编码
    @Column(name = "ACCT_CODE")
    private String acct_code;
    //借方科目
    @Column(name = "DEBIT")
    private String debit;
    //贷方科目
    @Column(name = "CREDIT")
    private String credit;
    //币种
    @Column(name = "CURRENCY")
    private String currency;
    //汇率
    @Column(name = "EX_RATE")
    private  String ex_rate;
    //税率
    @Column(name = "TAXRATE")
    private String taxrate;
    //税率金额
    @Column(name = "TAXRATE_AMOUNT")
    private Double taxrate_amount;
    //原币金额
    @Column(name = "ORICURRENCY_AMOUNT")
    private Double oricurrency_amount;
    //单位
    @Column(name = "UNIT")
    private String unit;
    //单价
    @Column(name = "UNIT_PRICE")
    private Double unit_price;
    //数量
    @Column(name = "QUANTITY")
    private Integer quantity;
    //金额
    @Column(name = "AMOUNT")
    private Double amount;
    //行号
    @Column(name = "ROWINDEX")
    private Double rowindex;
}
