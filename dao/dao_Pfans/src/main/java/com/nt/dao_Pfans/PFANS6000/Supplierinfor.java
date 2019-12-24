package com.nt.dao_Pfans.PFANS6000;

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
@Table(name = "supplierinfor")
public class Supplierinfor extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商信息
     */
    @Id
    @Column(name = "SUPPLIERINFOR_ID")
    private String supplierinfor_id;

    /**
     * 中文(客户信息详情)
     */
    @Column(name = "SUPCHINESE")
    private String supchinese;

    /**
     * 日文(客户信息详情)
     */
    @Column(name = "SUPJAPANESE")
    private String supjapanese;

    /**
     * 英文(客户信息详情)
     */
    @Column(name = "SUPENGLISH")
    private String supenglish;

    /**
     * 简称
     */
    @Column(name = "ABBREVIATION")
    private String abbreviation;

    /**
     * 负责人
     */
    @Column(name = "LIABLEPERSON")
    private String liableperson;

    /**
     * 项目联络人
     */
    @Column(name = "PROJECTPERSON")
    private String projectperson;

    /**
     * 中文(项目联络人)
     */
    @Column(name = "PROCHINESE")
    private String prochinese;

    /**
     * 日文(项目联络人)
     */
    @Column(name = "PROJAPANESE")
    private String projapanese;

    /**
     * 英文(项目联络人)
     */
    @Column(name = "PROENGLISH")
    private String proenglish;

    /**
     * 联系电话
     */
    @Column(name = "PROTELEPHONE")
    private String protelephone;

    /**
     * 电子邮箱
     */
    @Column(name = "PROTEMAIL")
    private String protemail;

    /**
     * 共通事务联络人
     */
    @Column(name = "COMMONTPERSON")
    private String commontperson;

    /**
     * 联系电话
     */
    @Column(name = "COMTELEPHONE")
    private String comtelephone;

    /**
     * 电子邮箱
     */
    @Column(name = "COMNEMAIL")
    private String comnemail;

    /**
     * 地址
     */
    @Column(name = "ADDRESS")
    private String address;

    /**
     * 中文(地址)
     */
    @Column(name = "ADDCHINESE")
    private String addchinese;

    /**
     * 日文(地址)
     */
    @Column(name = "ADDJAPANESE")
    private String addjapanese;

    /**
     * 英文(地址)
     */
    @Column(name = "ADDENGLISH")
    private String addenglish;

    /**
     * 人员规模
     */
    @Column(name = "PERSCALE")
    private String perscale;

    /**
     * 网址
     */
    @Column(name = "WEBSITE")
    private String website;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

}
