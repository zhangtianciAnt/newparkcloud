package com.nt.dao_Org;

import com.nt.utils.BaseModel;

import java.util.List;

/**
 * 个人信息
 */
public class UserInfo extends BaseModel {
    /**
     * 姓名
     */
    private String username;
    /**
     * 手机
     */
    private String mobilenumber;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 身份证
     */
    private String idnumber;
    /**
     * 性别
     */
    private String sex;
    /**
     * 来源渠道
     */
    private String source;
    /**
     * 客户阶段
     */
    private String customerstage;
    /**
     * 园区
     */
    private String parkid;
    /**
     * 公司
     */
    private List<String> companyid;
    /**
     * 部门
     */
    private List<String> departmentid;
    /**
     * 办公电话
     */
    private String tel;
    /**
     * 工号
     */
    private String jobnumber;
    /**
     * 头像
     */
    private String profilephoto;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCustomerstage() {
        return customerstage;
    }

    public void setCustomerstage(String customerstage) {
        this.customerstage = customerstage;
    }

    public String getParkid() {
        return parkid;
    }

    public void setParkid(String parkid) {
        this.parkid = parkid;
    }

    public List<String> getCompanyid() {
        return companyid;
    }

    public void setCompanyid(List<String> companyid) {
        this.companyid = companyid;
    }

    public List<String> getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(List<String> departmentid) {
        this.departmentid = departmentid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getJobnumber() {
        return jobnumber;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber;
    }

    public String getProfilephoto() {
        return profilephoto;
    }

    public void setProfilephoto(String profilephoto) {
        this.profilephoto = profilephoto;
    }
}
