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
@Table(name = "customerinfor")
public class Customerinfor extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 客户信息详情
     */
    @Id
    @Column(name = "CUSTOMERINFOR_ID")
    private String customerinfor_id;

    /**
     * 中文(客户信息详情)
     */
    @Column(name = "CUSTCHINESE")
    private String custchinese;

    /**
     * 日文(客户信息详情)
     */
    @Column(name = "CUSTJAPANESE")
    private String custjapanese;

    /**
     * 英文(客户信息详情)
     */
    @Column(name = "CUSTENGLISH")
    private String custenglish;

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

    /**
     * 附件
     */
    @Column(name = "UPLOADFILE")
    private String uploadfile;
}
