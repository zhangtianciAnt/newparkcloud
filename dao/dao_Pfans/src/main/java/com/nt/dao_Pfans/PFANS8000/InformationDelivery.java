package com.nt.dao_Pfans.PFANS8000;

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
public class InformationDelivery extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "INFORMATIONID")
    private String informationid;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "AVAILABLESTATE")
    private String availablestate;

    @Column(name = "RICHTEXT")
    private String richtext;

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
