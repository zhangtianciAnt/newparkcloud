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
@Table(name = "individual")
public class Individual extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "INDIVIDUAL_ID")
    private String individual_id;

    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    @Column(name = "CONTRACTTYPE")
    private String contracttype;

    @Column(name = "CUSTOJAPANESE")
    private String custojapanese;

    @Column(name = "LIABLEPERSON")
    private String liableperson;

    @Column(name = "ADDRESS")
    private String address;

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

    @Column(name = "PROJECTNAME")
    private String projectname;

    @Column(name = "DATES")
    private String dates;

}
