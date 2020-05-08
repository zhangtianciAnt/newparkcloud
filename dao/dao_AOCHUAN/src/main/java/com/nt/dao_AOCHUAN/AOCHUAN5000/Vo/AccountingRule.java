package com.nt.dao_AOCHUAN.AOCHUAN5000.Vo;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountingRule extends BaseModel {
    //分录规则id
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
    private Integer rowindex;
    //辅助核算项目id
    @Column(name = "AUXACCTG_ID")
    private String auxacctg_id;
    //外键：分录规则id
    @Column(name = "ACCTGRUL_FID")
    private String acctgrul_fid;
    //银行账号编码
    @Column(name = "BANKACCOUNT_CODE")
    private String bankaccount_code;
    //银行账号
    @Column(name = "BANKACCOUNT")
    private String bankaccount;
    //部门编码
    @Column(name = "DEPT_CODE")
    private String dept_code;
    //部门
    @Column(name = "DEPT")
    private String dept;
    //收支内容编码
    @Column(name = "IAE_CONTG_CODE")
    private String iae_contg_code;
    //收支内容
    @Column(name = "IAE_CONTG")
    private String iae_contg;
    //核算项目编码
    @Column(name = "AUXACCTG_CODE")
    private String auxacctg_code;
    //核算项目
    @Column(name = "AUXACCTG")
    private String auxacctg;
    //主现金流编码
    @Column(name = "MAINCASH_CODE")
    private String maincash_code;
    //主现金流项目
    @Column(name = "MAINCASHFLOW")
    private String maincashflow;
    //附现金流编码
    @Column(name = "ATTACHCASH_CODE")
    private String attachcash_code;
    //附现金流项目
    @Column(name = "ATTACHCASHFLOW")
    private String attachcashflow;
    //辅助账金额
    @Column(name = "AUXACCTG_AMOUNT")
    private String auxacctg_amount;
}
