package com.nt.dao_AOCHUAN.AOCHUAN3000;

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
@Table(name = "returngoods")
public class Returngoods extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RETURNGOODS_ID")
    private String returngoods_id;


    @Column(name = "CONTRACTNO")
    private String contractno;


    @Column(name = "RETURNREASON")
    private String returnreason;


    @Column(name = "RETURNAGREEMENT")
    private Integer returnagreement;


    @Column(name = "TESTREPORT")
    private Integer testreport;


    @Column(name = "RETURNWAYBILL")
    private Integer returnwaybill;


    @Column(name = "FACTORYRETURN")
    private Integer factoryreturn;


    @Column(name = "COSTCONFIRMATION")
    private Integer costconfirmation;


    @Column(name = "SCARLETLETTER")
    private Integer scarletletter;


    @Column(name = "NOTICEARRIVAL")
    private Integer noticearrival;


    @Column(name = "DOCUMENTS")
    private Integer documents;


    @Column(name = "LADING")
    private Integer lading;


    @Column(name = "PAYMENT")
    private Integer payment;

    @Column(name = "FACTORYCOMMITMENT")
    private String factorycommitment;


    @Column(name = "AOCHUANCOMMITMENT")
    private String aochuancommitment;

    @Column(name = "TYPE")
    //type = 0 销售询单录入 ,type = 1 采购待确认 ,type = 2 单据待确认 ，type = 3 完结
    private Integer type;

    @Transient
    private boolean notice;

}
