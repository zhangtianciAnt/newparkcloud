package com.nt.dao_Org;

import com.nt.utils.BaseModel;

/**
 * 公司开票信息
 */
public class InvoiceInfo extends BaseModel {
    /**
     * 公司开票信息id
     */
    private String invoiceinfoid;
    /**
     * 公司名称
     */
    private String companyname;
    /**
     * 纳税人识别号
     */
    private String dutynumber;
    /**
     * 地址
     */
    private String companyaddress;
    /**
     * 电话
     */
    private String phone;
    /**
     * 银行名称
     */
    private String bankname;
    /**
     * 开户行
     */
    private String bankbranch;
    /**
     * 账号
     */
    private String banknumber;

    public String getInvoiceinfoid() {
        return invoiceinfoid;
    }

    public void setInvoiceinfoid(String invoiceinfoid) {
        this.invoiceinfoid = invoiceinfoid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getDutynumber() {
        return dutynumber;
    }

    public void setDutynumber(String dutynumber) {
        this.dutynumber = dutynumber;
    }

    public String getCompanyaddress() {
        return companyaddress;
    }

    public void setCompanyaddress(String companyaddress) {
        this.companyaddress = companyaddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankbranch() {
        return bankbranch;
    }

    public void setBankbranch(String bankbranch) {
        this.bankbranch = bankbranch;
    }

    public String getBanknumber() {
        return banknumber;
    }

    public void setBanknumber(String banknumber) {
        this.banknumber = banknumber;
    }
}
