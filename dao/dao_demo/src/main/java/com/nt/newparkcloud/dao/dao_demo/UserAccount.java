package com.nt.newparkcloud.dao.dao_demo;

import com.nt.newparkcloud.utils.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "useraccount")
public class UserAccount extends BaseModel{
    /**
     * 用户id
     */
    private String userid;
    /**
     * 账号
     */
    private String accound;
    /**
     * 密码
     */
    private String password;
    /**
     * openid
     */
    private String openid;
    /**
     * 账户类型
     */
    private String usertype;
    /**
     * 个人信息
     */
    private UserInfo userinfo;
    /**
     * 企业信息
     */
    private CompanyInfo companyinfo;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAccound() {
        return accound;
    }

    public void setAccound(String accound) {
        this.accound = accound;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
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

    /**
     * 个人信息
     */
    private class UserInfo extends BaseModel{
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
         * 密码
         */
        private String password;
        /**
         * 性别
         */
        private String sex;
        /**
         * 园区
         */
        private List<String> parkid;
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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public List<String> getParkid() {
            return parkid;
        }

        public void setParkid(List<String> parkid) {
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

    /**
     * 企业信息
     */
    private class CompanyInfo extends BaseModel{
        /**
         * 企业名称
         */
        private String companyname;
        /**
         * 企业logo
         */
        private String companylogo;
        /**
         * 信用编码
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
         * 联系人姓名
         */
        private String contactusername;
        /**
         * 联系人电话
         */
        private String contactmobile;

        public String getCompanyname() {
            return companyname;
        }

        public void setCompanyname(String companyname) {
            this.companyname = companyname;
        }

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

        public String getContactusername() {
            return contactusername;
        }

        public void setContactusername(String contactusername) {
            this.contactusername = contactusername;
        }

        public String getContactmobile() {
            return contactmobile;
        }

        public void setContactmobile(String contactmobile) {
            this.contactmobile = contactmobile;
        }
    }
}
