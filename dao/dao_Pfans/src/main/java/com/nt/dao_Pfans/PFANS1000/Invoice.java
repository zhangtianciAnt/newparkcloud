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
@Table(name = "invoice")
public class Invoice extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "INVOICE_ID" )
    private String invoice_id;

    @Column(name = "PUBLICEXPENSE_ID")
    private String publicexpenseid;

    @Column(name = "EVECTION_ID")
    private String evectionid;

    @Column(name = "INVOICENUMBER")
    private String invoicenumber;

    @Column(name = "INVOICETYPE")
    private String invoicetype;


    @Column(name = "INVOICEAMOUNT")
    private String invoiceamount;

    @Column(name = "TAXRATE")
    private String taxrate;

    @Column(name = "EXCLUDINGTAX")
    private String excludingtax;

    @Column(name = "FACETAX")
    private String facetax;

    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
