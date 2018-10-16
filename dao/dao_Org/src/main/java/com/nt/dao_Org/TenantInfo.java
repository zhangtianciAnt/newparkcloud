package com.nt.dao_Org;

import com.nt.utils.BaseModel;

import java.util.Date;

/**
 * 租户信息
 */
public class TenantInfo extends BaseModel {
    /**
     * 企业logo
     */
    private String companylogo;
    /**
     * 统一社会信息代码
     */
    private String creditcode;
    /**
     * 公司地址
     */
    private String companyaddress;
    /**
     * 公司法人
     */
    private String companycorporation;
    /**
     * 公司执照
     */
    private String unifedsocialcreditcode;
    /**
     * 公司成立日期
     */
    private Date establish;
    /**
     * 门户网址
     */
    private String portalurl;

    public String getCompanylogo() {
        return companylogo;
    }

    public void setCompanylogo(String companylogo) {
        this.companylogo = companylogo;
    }

    public String getCreditcode() {
        return creditcode;
    }

    public void setCreditcode(String creditcode) {
        this.creditcode = creditcode;
    }

    public String getCompanyaddress() {
        return companyaddress;
    }

    public void setCompanyaddress(String companyaddress) {
        this.companyaddress = companyaddress;
    }

    public String getCompanycorporation() {
        return companycorporation;
    }

    public void setCompanycorporation(String companycorporation) {
        this.companycorporation = companycorporation;
    }

    public String getUnifedsocialcreditcode() {
        return unifedsocialcreditcode;
    }

    public void setUnifedsocialcreditcode(String unifedsocialcreditcode) {
        this.unifedsocialcreditcode = unifedsocialcreditcode;
    }

    public Date getEstablish() {
        return establish;
    }

    public void setEstablish(Date establish) {
        this.establish = establish;
    }

    public String getPortalurl() {
        return portalurl;
    }

    public void setPortalurl(String portalurl) {
        this.portalurl = portalurl;
    }
}
