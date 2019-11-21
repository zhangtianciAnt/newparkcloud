package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "otherfive")
public class OtherFive extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "OTHERFIVE_ID")
    private String otherfive_id;
    
    @Column(name = "GIVING_ID")
    private String giving_id;

    @Column(name = "DEPARTMENT_ID")
    private String department_id;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "MEDICALINSURANCE")
    private String medicalinsurance;

    @Column(name = "ACCIDENT")
    private String accident;

    @Column(name = "PHYSICAL")
    private String physical;

    @Column(name = "WELFARETOTAL")
    private String welfaretotal;

    @Column(name = "LABOURUNION")
    private String labourunion;

    @Column(name = "ANNUALMEETING")
    private String annualmeeting;

    @Column(name = "TRAVEL")
    private String travel;

    @Column(name = "TOTAL")
    private String total;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
