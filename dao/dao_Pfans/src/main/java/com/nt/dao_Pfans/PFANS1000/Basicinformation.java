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
@Table(name = "basicinformation")
public class Basicinformation extends BaseModel {

	private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "BASICINFORMATION_ID")
    private String basicinformationid;

    @Id
    @Column(name = "QUOTATION_ID")
    private String quotationid;

    @Column(name = "CLAIMTYPE")
    private String claimtype;

    @Column(name = "DELIVERYDATE")
    private Date deliverydate;

    @Column(name = "COMPLETIONDATE")
    private Date completiondate;

    @Column(name = "CLAIMDATE")
    private Date claimdate;

    @Column(name = "SUPPORTDATE")
    private Date supportdate;

    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
