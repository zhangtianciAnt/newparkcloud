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
@Table(name = "trafficdetails")
public class TrafficDetails extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TRAFFICDETAILS_ID")
    private String trafficdetails_id;

    @Column(name = "PUBLICEXPENSE_ID")
    private String publicexpenseid;

    @Column(name = "EVECTION_ID")
    private String evectionid;

    @Column(name = "TRAFFICDATE")
    private Date trafficdate;

    @Column(name = "REGION")
    private String region;

    @Column(name = "VEHICLE")
    private String vehicle;

    @Column(name = "INVOICENUMBER")
    private String invoicenumber;

    @Column(name = "COSTITEM")
    private String costitem;

    @Column(name = "DEPARTMENTNAME")
    private String departmentname;

    @Column(name = "STARTINGPOINT")
    private String startingpoint;

    @Column(name = "RMB")
    private String rmb;

    @Column(name = "FOREIGNCURRENCY")
    private String foreigncurrency;

    @Column(name = "ANNEXNO")
    private String annexno;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
