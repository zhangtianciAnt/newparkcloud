package com.nt.dao_AOCHUAN.AOCHUAN7000.Vo;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class All {

    @Column(name = "CRERULE_ID")
    private String crerule_id;

    @Column(name = "DOCURULE_FID")
    private String docurule_fid;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "DEBIT")
    private String debit;

    @Column(name = "CREDIT")
    private String credit;

    @Column(name = "CRERATE")
    private String crerate;

    @Column(name = "HELPRULE_ID")
    private String helprule_id;

    @Column(name = "CRERULE_FID")
    private String crerule_fid;

    @Column(name = "BANKACCOUNT")
    private String bankaccount;

    @Column(name = "DEPART")
    private String depart;

    @Column(name = "EXPENDITURE")
    private String expenditure;

    @Column(name = "ACCOUNTING")
    private String accounting;

    @Column(name = "MAINCASH")
    private String maincash;

    @Column(name = "FLOWCASH")
    private String flowcash;

    @Column(name = "ROWINDEX")
    private int rowindex;

    @Column(name = "ACCOUNTID")
    private String accountid;

    @Column(name = "BANKACCOUNTID")
    private String bankaccountid;

    @Column(name = "DEPARTID")
    private String departid;

    @Column(name = "EXPENDITUREID")
    private String expenditureid;

    @Column(name = "ACCOUNTINGID")
    private String accountingid;

    @Column(name = "MAINCASHID")
    private String maincashid;

    @Column(name = "FLOWCASHID")
    private String flowcashid;

    @Column(name = "CRERULE_WID")
    private String crerule_wid;

    @Column(name = "CREATEON")
    private String createon;

    @Column(name = "CREATEBY")
    private String createby;

    @Column(name = "STATUS")
    private String status;

}
