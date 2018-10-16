package com.nt.dao_Org;

import com.nt.utils.BaseModel;

import java.util.Date;

/**
 * 工商信息
 */
public class IndustryInfo extends BaseModel {
    /**
     * 统一社会信息代码
     */
    private String creditcode;
    /**
     * 纳税人识别号
     */
    private String dutynumber;
    /**
     * 注册号
     */
    private String registrationnumber;
    /**
     * 组织机构代码
     */
    private String organizationcode;
    /**
     * 法定代表人
     */
    private String legalrepresentative;
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 注册资本
     */
    private String registeredcapital;
    /**
     * 经营状态
     */
    private String operatingstate;
    /**
     * 成立时间
     */
    private Date timeofestablishment;
    /**
     * 公司类型
     */
    private String typeofcompany;
    /**
     * 人员规模
     */
    private String personnelscale;
    /**
     * 经营期限
     */
    private String termofoperation;
    /**
     * 登记机关
     */
    private String registrationauthority;
    /**
     * 核准日期
     */
    private Date dateofapproval;
    /**
     * 英文名
     */
    private String englishname;
    /**
     * 所属地区
     */
    private String affiliatedarea;
    /**
     * 所属行业
     */
    private String industry;
    /**
     * 注册地址
     */
    private String registeredaddress;
    /**
     * 经营范围
     */
    private String scopeofoperation;

    public String getCreditcode() {
        return creditcode;
    }

    public void setCreditcode(String creditcode) {
        this.creditcode = creditcode;
    }

    public String getDutynumber() {
        return dutynumber;
    }

    public void setDutynumber(String dutynumber) {
        this.dutynumber = dutynumber;
    }

    public String getRegistrationnumber() {
        return registrationnumber;
    }

    public void setRegistrationnumber(String registrationnumber) {
        this.registrationnumber = registrationnumber;
    }

    public String getOrganizationcode() {
        return organizationcode;
    }

    public void setOrganizationcode(String organizationcode) {
        this.organizationcode = organizationcode;
    }

    public String getLegalrepresentative() {
        return legalrepresentative;
    }

    public void setLegalrepresentative(String legalrepresentative) {
        this.legalrepresentative = legalrepresentative;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getRegisteredcapital() {
        return registeredcapital;
    }

    public void setRegisteredcapital(String registeredcapital) {
        this.registeredcapital = registeredcapital;
    }

    public String getOperatingstate() {
        return operatingstate;
    }

    public void setOperatingstate(String operatingstate) {
        this.operatingstate = operatingstate;
    }

    public Date getTimeofestablishment() {
        return timeofestablishment;
    }

    public void setTimeofestablishment(Date timeofestablishment) {
        this.timeofestablishment = timeofestablishment;
    }

    public String getTypeofcompany() {
        return typeofcompany;
    }

    public void setTypeofcompany(String typeofcompany) {
        this.typeofcompany = typeofcompany;
    }

    public String getPersonnelscale() {
        return personnelscale;
    }

    public void setPersonnelscale(String personnelscale) {
        this.personnelscale = personnelscale;
    }

    public String getTermofoperation() {
        return termofoperation;
    }

    public void setTermofoperation(String termofoperation) {
        this.termofoperation = termofoperation;
    }

    public String getRegistrationauthority() {
        return registrationauthority;
    }

    public void setRegistrationauthority(String registrationauthority) {
        this.registrationauthority = registrationauthority;
    }

    public Date getDateofapproval() {
        return dateofapproval;
    }

    public void setDateofapproval(Date dateofapproval) {
        this.dateofapproval = dateofapproval;
    }

    public String getEnglishname() {
        return englishname;
    }

    public void setEnglishname(String englishname) {
        this.englishname = englishname;
    }

    public String getAffiliatedarea() {
        return affiliatedarea;
    }

    public void setAffiliatedarea(String affiliatedarea) {
        this.affiliatedarea = affiliatedarea;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getRegisteredaddress() {
        return registeredaddress;
    }

    public void setRegisteredaddress(String registeredaddress) {
        this.registeredaddress = registeredaddress;
    }

    public String getScopeofoperation() {
        return scopeofoperation;
    }

    public void setScopeofoperation(String scopeofoperation) {
        this.scopeofoperation = scopeofoperation;
    }
}
