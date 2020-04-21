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

    @Column(name = "DOCURULE_ID")
    private String docurule_id;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "DEBIT")
    private String debit;

    @Column(name = "CREDIT")
    private String credit;

    @Column(name = "RATE")
    private String rate;

    @Column(name = "SUACCOUNTING")
    private String suaccounting;
}
