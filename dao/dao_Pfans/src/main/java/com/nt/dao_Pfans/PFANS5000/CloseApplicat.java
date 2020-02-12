package com.nt.dao_Pfans.PFANS5000;

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
@Table(name = "closeapplicat")
public class CloseApplicat extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CLOSEAPPLICAT_ID")
    private String closeapplicatid;

    @Column(name = "PROJECTNUMBER")
    private String projectnumber;

    @Column(name = "PROJECTNAME")
    private String projectname;

    @Column(name = "PROJECTSTYPE")
    private String projectstype;

    @Column(name = "STARTDATE")
    private Date startdate;

    @Column(name = "SECRET")
    private String secret;

    @Column(name = "DATETIME")
    private String datetime;

    @Column(name = "MANAGER")
    private String manager;

    @Column(name = "PREDICTDATE")
    private Date predictdate;

    @Column(name = "REALDATE")
    private Date realdate;

    @Column(name = "TESTDATE")
    private Date testdate;

    @Column(name = "REALPEOPEL")
    private String realpeopel;

    @Column(name = "REPORTNUMBER")
    private String reportnumber;

    @Column(name = "ASSETADDRESS")
    private String assetaddress;

    @Column(name = "EXPLAN")
    private String explan;

    @Column(name = "EXPRENCE")
    private String exprence;

    @Column(name = "ADVISE")
    private String advise;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "UPLOADFILE")
    private String uploadfile;


}
