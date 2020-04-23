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
@Table(name = "helprule")
public class Helprule extends BaseModel {

    @Id
    @Column(name = "HELPRULE_ID")
    private String helprule_id;

    @Column(name = "CRERULE_ID")
    private String crerule_id;

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
    private String rowindex;

}
