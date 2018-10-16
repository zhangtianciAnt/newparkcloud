package com.nt.dao_Org;


import com.nt.utils.BaseModel;

/**
 * 公司账户信息
 */
public class BankInfo extends BaseModel {
    /**
     * 银行账户id
     */
    private String bankaccid;
    /**
     * 银行名称
     */
    private String bankname;
    /**
     * 开户行
     */
    private String bankbranch;
    /**
     * 银行账号
     */
    private String banknumber;
    /**
     * 公司id
     */
    private String companyid;

    public String getBankaccid() {
        return bankaccid;
    }

    public void setBankaccid(String bankaccid) {
        this.bankaccid = bankaccid;
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

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }
}
