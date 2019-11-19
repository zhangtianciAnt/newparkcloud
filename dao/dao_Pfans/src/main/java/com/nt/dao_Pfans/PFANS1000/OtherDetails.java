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
    private String publicexpense_id;

    /**
     * 境内/外出差精算ID
     */
    @Column(name = "EVECTION_ID")
    private String evectionid;

    @Column(name = "JUDGEMENT_ID")
    private String judgement_id;

    @Column(name = "TRAFFICDATE")
    private Date trafficdate;


    @Column(name = "COSTITEM")
    private String costitem;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "RMB")
    private String rmb;

    @Column(name = "FOREIGNCURRENCY")
    private String foreigncurrency;

    @Column(name = "ANNEXNO")
    private String annexno;

    @Column(name = "ROWINDEX")
    private Integer rowindex;


}