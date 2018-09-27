package com.nt.newparkcloud.dao.dao_demo;

import com.nt.newparkcloud.utils.BaseModel;

import java.util.List;

/**
 * 企业信息
 */
public class CompanyInfo extends BaseModel {
    /**
     * 企业名称
     */
    private String companyname;
    /**
     * 行业分类
     */
    private String industrytype;
    /**
     * 来源渠道
     */
    private String source;
    /**
     * 标签
     */
    private String label;
    /**
     * 客户阶段
     */
    private String customerstage;
    /**
     * 租户信息
     */
    private TenantInfo tenantinfo;
    /**
     * 联系人信息
     */
    private List<ContactInfo> contactinfo;
    /**
     * 开票信息
     */
    private InvoiceInfo invoiceinfo;
    /**
     * 工商信息
     */
    private IndustryInfo industryinfo;

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getIndustrytype() {
        return industrytype;
    }

    public void setIndustrytype(String industrytype) {
        this.industrytype = industrytype;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCustomerstage() {
        return customerstage;
    }

    public void setCustomerstage(String customerstage) {
        this.customerstage = customerstage;
    }

    public TenantInfo getTenantinfo() {
        return tenantinfo;
    }

    public void setTenantinfo(TenantInfo tenantinfo) {
        this.tenantinfo = tenantinfo;
    }

    public List<ContactInfo> getContactinfo() {
        return contactinfo;
    }

    public void setContactinfo(List<ContactInfo> contactinfo) {
        this.contactinfo = contactinfo;
    }

    public InvoiceInfo getInvoiceinfo() {
        return invoiceinfo;
    }

    public void setInvoiceinfo(InvoiceInfo invoiceinfo) {
        this.invoiceinfo = invoiceinfo;
    }

    public IndustryInfo getIndustryinfo() {
        return industryinfo;
    }

    public void setIndustryinfo(IndustryInfo industryinfo) {
        this.industryinfo = industryinfo;
    }
}
