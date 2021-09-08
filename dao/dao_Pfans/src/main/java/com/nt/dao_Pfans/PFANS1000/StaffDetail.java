package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "staffdetail")
public class StaffDetail extends BaseModel {
    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "STAFFDETAIL_ID")
    private String staffdetail_id;

    @Column(name = "AWARD_ID")
    private String award_id;

    //rank
    @Column(name = "ATTF")
    private String attf;

    @Column(name = "BUDGETCODE")
    private String budgetcode;

    @Column(name = "SUBTOTAL")
    private String subtotal;

    @Column(name = "DEPART")
    private String depart;

    //合同号
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    //部门
    @Column(name = "INCONDEPARTMENT")
    private String incondepartment;

    //4
    @Column(name = "INWORK04")
    private String inwork04;

    //5
    @Column(name = "INWORK05")
    private String inwork05;

    //6
    @Column(name = "INWORK06")
    private String inwork06;

    //7
    @Column(name = "INWORK07")
    private String inwork07;

    //8
    @Column(name = "INWORK08")
    private String inwork08;

    //9
    @Column(name = "INWORK09")
    private String inwork09;

    //10
    @Column(name = "INWORK10")
    private String inwork10;

    //11
    @Column(name = "INWORK11")
    private String inwork11;

    //12
    @Column(name = "INWORK12")
    private String inwork12;

    //1
    @Column(name = "INWORK01")
    private String inwork01;

    //2
    @Column(name = "INWORK02")
    private String inwork02;

    //3
    @Column(name = "INWORK03")
    private String inwork03;

    @Column(name = "REASON")
    private String reason;

    //合同金额
    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

    //计算成本
    @Transient
    private String bm;
}
