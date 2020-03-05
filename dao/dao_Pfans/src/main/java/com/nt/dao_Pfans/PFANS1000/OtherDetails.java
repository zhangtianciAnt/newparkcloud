package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "otherdetails")
public class OtherDetails extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OTHERDETAILS_ID" )
    private String otherdetails_id;

    @Column(name = "PUBLICEXPENSE_ID")
    private String publicexpenseid;

    @Column(name = "EVECTION_ID")
    private String evectionid;

    @Column(name = "OTHERDETAILSDATE")
    private Date otherdetailsdate;


    @Column(name = "COSTITEM")
    private String costitem;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "RMB")
    private String rmb;

    @Column(name = "INVOICENUMBER")
    private String invoicenumber;

    @Column(name = "DEPARTMENTNAME")
    private String departmentname;

    @Column(name = "SUBJECTNUMBER")
    private String subjectnumber;

    @Column(name = "ACCOUNTCODE")
    private String accountcode;

    @Column(name = "BUDGETCODING")
    private String budgetcoding;

    @Column(name = "FOREIGNCURRENCY")
    private String foreigncurrency;

    @Column(name = "ANNEXNO")
    private String annexno;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

    @Column(name = "TAXES")
    private Integer taxes;
}
