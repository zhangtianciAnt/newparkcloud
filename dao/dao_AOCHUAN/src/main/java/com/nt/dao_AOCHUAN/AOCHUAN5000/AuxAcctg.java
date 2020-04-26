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
@Table(name = "auxacctg")
public class AuxAcctg extends BaseModel {

    //主键
    @Id
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

    //附现金流i项目
    @Column(name = "ATTACHCASHFLOW")
    private String attachcashflow;

    //辅助账金额
    @Column(name = "AUXACCTG_AMOUNT")
    private String auxacctg_amount;
}
