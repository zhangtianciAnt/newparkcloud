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
@Table(name = "information")
public class Flexiblework extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CENTER_ID")
    private String center_id;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "TEAM_ID")
    private String team_id;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "APPLICATION_DATE")
    private String application_date;

    @Column(name = "WORKTIME")
    private String worktime;

    @Column(name = "IMPLEMENT_DATE")
    private String implement_date;



//    @Column(name = "CREATEBY")
//    private String createby;
//
//    @Column(name = "CREATEON")
//    private Date createon;
//
//    @Column(name = "MODIFYBY")
//    private String modifyby;
//
//    @Column(name = "MODIFYON")
//    private Date modifyon;
//
//    @Column(name = "OWNER")
//    private String owner;

//    @Column(name = "STATUS")
//    private String status;



}
