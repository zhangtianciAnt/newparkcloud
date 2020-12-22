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

    @Column(name = "APPLICATION_DATE ")
    private Date application_date;

    @Column(name = "USEDATE" )
    private Date usedate;

    @Column(name = "PRINTSCORE ")
    private String printscore;

    @Column(name = "FILENAME ")
    private String filename;

    @Column(name = "CLIENT ")
    private String client;

    @Column(name = "FILETYPE ")
    private String filetype;

    @Column(name = "SEALTYPE ")
    private String sealtype;


    @Column(name = "REMARKS ")
    private String remarks;

    //书类ID
    @Column(name = "BOOKID ")
    private String bookid;
    //add-ws-7/20-禅道任务342
    //决裁ID
    @Column(name = "AWARDID ")
    private String awardid;
    //add-ws-7/20-禅道任务342
    //add-ws-12/21-印章盖印
    //    承认者
    @Column(name = "ACCEPTOR")
    private String acceptor;
    //    盖印承认
    @Column(name = "ACCEPTSTATE")
    private String acceptstate;
    //    盖印监管确认
    @Column(name = "REGULATORSTATE")
    private String regulatorstate;
    //    盖印监管者
    @Column(name = "REGULATOR")
    private String regulator;
    //add-ws-12/21-印章盖印
}

