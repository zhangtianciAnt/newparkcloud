package com.nt.dao_Pfans.PFANS6000;

import com.nt.utils.Encryption.Encryption;
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
    @Encryption
    @Column(name = "CUSTCHINESE")
    private String custchinese;

    /**
     * 日文(客户信息详情)
     */
    @Encryption
    @Column(name = "CUSTJAPANESE")
    private String custjapanese;

    /**
     * 英文(客户信息详情)
     */
    @Encryption
    @Column(name = "CUSTENGLISH")
    private String custenglish;

    /**
     * 简称
     */
    @Column(name = "ABBREVIATION")
    private String abbreviation;

    /**
     * 公司法人
     */
    @Column(name = "LIABLEPERSON")
    private String liableperson;

    /**
     * 所属公司
     */
    @Column(name = "THECOMPANY")
    private String thecompany;

    /**
     * 事业场编码
     */
    @Column(name = "CAUSECODE")
    private String causecode;

    /**
     * 地域区分
     */
    @Column(name = "REGINDIFF")
    private String regindiff;

    /**
     * 所属部门(中)
     */
    @Column(name = "THEDEPC")
    private String thedepC;

    /**
     * 所属部门(英)
     */
    @Column(name = "THEDEPE")
    private String thedepE;

    /**
     * 所属部门(日)
     */
    @Column(name = "THEDEPJ")
    private String thedepJ;

    /**
     * 客户信息主表id
     */
    @Column(name = "CUSTOMERINFORPRIMARY_ID")
    private String customerinforprimary_id;

    /**
     * 联络人(中文)
     */
    @Encryption
    @Column(name = "PROCHINESE")
    private String prochinese;

    /**
     * 联络人(日文)
     */
    @Encryption
    @Column(name = "PROJAPANESE")
    private String projapanese;

    /**
     * 联络人(英文)
     */
    @Encryption
    @Column(name = "PROENGLISH")
    private String proenglish;

    /**
     * 联系电话
     */
    @Encryption
    @Column(name = "PROTELEPHONE")
    private String protelephone;

    /**
     * 电子邮箱
     */
    @Encryption
    @Column(name = "PROTEMAIL")
    private String protemail;

    /**
     * 中文(地址)
     */
    @Encryption
    @Column(name = "ADDCHINESE")
    private String addchinese;

    /**
     * 日文(地址)
     */
    @Encryption
    @Column(name = "ADDJAPANESE")
    private String addjapanese;

    /**
     * 英文(地址)
     */
    @Encryption
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
     * 暂时无用
     */
    @Column(name = "UPLOADFILE")
    private String uploadfile;

    /**
     * 暂时无用
     */
    @Encryption
    @Column(name = "COMMONTPERSON")
    private String commontperson;

    /**
     * 暂时无用
     */
    @Encryption
    @Column(name = "COMTELEPHONE")
    private String comtelephone;

    /**
     * 暂时无用
     */
    @Encryption
    @Column(name = "COMNEMAIL")
    private String comnemail;


}
