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
    //借方科目
    @Column(name = "DEBIT")
    private String debit;
    //贷方科目
    @Column(name = "CREDIT")
    private String credit;
    //税率
    @Column(name = "TAXRATE")
    private String taxrate;
    //金额
    @Column(name = "AMOUNT")
    private String amount;
}
