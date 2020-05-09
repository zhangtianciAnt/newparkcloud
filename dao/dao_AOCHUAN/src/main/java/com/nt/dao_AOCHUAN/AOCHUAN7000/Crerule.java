package com.nt.dao_AOCHUAN.AOCHUAN7000;

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
@Table(name = "crerule")
public class Crerule  extends BaseModel {
    @Id
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

    @Column(name = "ROWINDEX")
    private int rowindex;

    @Column(name = "ACCOUNTID")
    private String accountid;

    @Column(name = "AMOUNTTYPE")
    private String amounttype;

    @Column(name = "UNITID")
    private String unitid;

    @Column(name = "UNITNUMBER")
    private String unitnumber;

    @Column(name = "UNITNAME")
    private String unitname;

}
