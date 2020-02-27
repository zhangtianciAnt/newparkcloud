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

    @Column(name = "DELIVERY")
    private String delivery;

    @Column(name = "DELIVERYAATE")
    private Date deliveryaate;

    @Column(name = "FINISHEDDAY")
    private Date finishedday;

    @Column(name = "SUPPORTDAY")
    private Date supportday;

    @Column(name = "REQUESTDAY")
    private Date requestday;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
