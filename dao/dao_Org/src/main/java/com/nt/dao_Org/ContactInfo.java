package com.nt.dao_Org;

import com.nt.utils.BaseModel;

/**
 * 联系人信息
 */
public class ContactInfo extends BaseModel {
    /**
     * 姓名
     */
    private String contactname;
    /**
     * 性别
     */
    private String sex;
    /**
     * 职位
     */
    private String job;
    /**
     * 手机
     */
    private String mobilenumber;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话
     */
    private String tel;

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
