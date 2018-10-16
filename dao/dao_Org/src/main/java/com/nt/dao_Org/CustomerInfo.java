package com.nt.dao_Org;

import com.nt.utils.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 个人信息
 */
@Document(collection = "customerinfo")
public class CustomerInfo extends BaseModel {
    /**
     * 客户id
     */
    private String customerid;
    /**
     * 用户id
     */
    private String userid;
    /**
     * 客户类型
     */
    private String type;
    /**
     * 个人信息
     */
    private UserInfo userinfo;
    /**
     * 企业信息
     */
    private CompanyInfo companyinfo;

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public CompanyInfo getCompanyinfo() {
        return companyinfo;
    }

    public void setCompanyinfo(CompanyInfo companyinfo) {
        this.companyinfo = companyinfo;
    }
}
