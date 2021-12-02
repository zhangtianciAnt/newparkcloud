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
@Table(name = "customerinforprimary")
public class CustomerinforPrimary extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 客户信息主表id
     */
    @Id
    @Column(name = "CUSTOMERINFORPRIMARY_ID")
    private String customerinforprimary_id;

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

}
