package com.nt.dao_Pfans.PFANS6000;

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
@Table(name = "bpnewsagency")
public class CompanyStatistics extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BPNEWSAGENCY_ID")
    private String bpnewsagency_id;

    @Column(name = "BPCOMPANY")
    private String bpcompany;

    @Column(name = "MANHOUR4")
    private String manhour4;

    @Column(name = "COST4")
    private String cost4;

    @Column(name = "MANHOUR5")
    private String manhour5;

    @Column(name = "COST5")
    private String cost5;

    @Column(name = "MANHOUR6")
    private String manhour6;

    @Column(name = "COST6")
    private String cost6;

    @Column(name = "MANHOUR7")
    private String manhour7;

    @Column(name = "COST7")
    private String cost7;

    @Column(name = "MANHOUR8")
    private String manhour8;

    @Column(name = "COST8")
    private String cost8;

    @Column(name = "MANHOUR9")
    private String manhour9;

    @Column(name = "COST9")
    private String cost9;

    @Column(name = "MANHOUR10")
    private String manhour10;

    @Column(name = "COST10")
    private String cost10;

    @Column(name = "MANHOUR11")
    private String manhour11;

    @Column(name = "COST11")
    private String cost11;

    @Column(name = "MANHOUR12")
    private String manhour12;

    @Column(name = "COST12")
    private String cost12;

    @Column(name = "MANHOUR1")
    private String manhour1;

    @Column(name = "COST1")
    private String cost1;

    @Column(name = "MANHOUR2")
    private String manhour2;

    @Column(name = "COST2")
    private String cost2;

    @Column(name = "MANHOUR3")
    private String manhour3;

    @Column(name = "COST3")
    private String cost3;

    @Column(name = "TOTALMANHOURS")
    private String totalmanhours;

    @Column(name = "TOTALCOST")
    private String totalcost;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "STATE")
    private String state;

    @Column(name = "ROWINDEX")
    private String rowindex;
}
