package com.nt.dao_Pfans.PFANS4000;

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
@Table(name = "seal")

public class Seal extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "SEAL_ID")
    private String sealid;


    @Column(name = "USER_ID")
    private String userid;


    @Column(name = "CENTER_ID")
    private String centerid;


    @Column(name = "GROUP_ID")
    private String groupid;

    @Column(name = "TEAM_ID")
    private String teamid;


    @Column(name = "PRINTSCORE ")
    private Date printscode;

    @Column(name = "FILENAME ")
    private Date filename;

    @Column(name = "CLIENT ")
    private Date client;

    @Column(name = "FILETYPE ")
    private Date filetype;

    @Column(name = "SEALTYPE ")
    private Date sealtype;

    @Column(name = "APPLICATIONNUMBER ")
    private Date applicationnumber;

    @Column(name = "REMARKS ")
    private Date remarks;

}