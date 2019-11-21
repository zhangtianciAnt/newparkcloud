package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "purchasedetails")
public class PurchaseDetails extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PURCHASEDETAILS_ID")
    private String purchasedetails_id;

    @Column(name = "PUBLICEXPENSE_ID")
    private String publicexpenseid;

    @Column(name = "PURCHASEDETAILSDATE")
    private Date purchasedetailsdate;

    @Column(name = "PROCUREMENTPROJECT")
    private String procurementproject;

    @Column(name = "PROCUREMENTDETAILS")
    private String procurementdetails;

    @Column(name = "RMB")
    private String rmb;

    @Column(name = "FOREIGNCURRENCY")
    private String foreigncurrency;

    @Column(name = "ANNEXNO")
    private String annexno;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
