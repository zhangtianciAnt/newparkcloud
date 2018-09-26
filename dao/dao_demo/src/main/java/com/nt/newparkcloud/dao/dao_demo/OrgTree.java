package com.nt.newparkcloud.dao.dao_demo;

import com.nt.newparkcloud.utils.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "orgtree")
public class OrgTree extends BaseModel{
    /**
     * 节点id
     */
    private String nodeid;
    /**
     * 节点标题
     */
    private String title;
    /**
     * 节点类型：1：公司；2：部门
     */
    private String type;
    /**
     * 组织id
     */
    private String orgid;
    /**
     * 公司id
     */
    private String companyid;
    /**
     * 公司简称
     */
    private String companyshortname;
    /**
     * 公司英文名称
     */
    private String companyen;
    /**
     * 公司全称
     */
    private String companyname;
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
     * 公司成立时间
     */
    private Date establish;
    /**
     * 部门id
     */
    private String departmentid;
    /**
     * 部门名称
     */
    private String departmentname;
    /**
     * 公司开票信息
     */
    private InvoiceInfo invoiceinfo;
    /**
     * 公司账户信息
     */
    private List<BankInfo> bankinfo;
    /**
     * 子公司信息
     */
    private List<OrgTree> orgs;

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCompanyshortname() {
        return companyshortname;
    }

    public void setCompanyshortname(String companyshortname) {
        this.companyshortname = companyshortname;
    }

    public String getCompanyen() {
        return companyen;
    }

    public void setCompanyen(String companyen) {
        this.companyen = companyen;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
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

    public InvoiceInfo getInvoiceinfo() {
        return invoiceinfo;
    }

    public void setInvoiceinfo(InvoiceInfo invoiceinfo) {
        this.invoiceinfo = invoiceinfo;
    }

    public List<BankInfo> getBankinfo() {
        return bankinfo;
    }

    public void setBankinfo(List<BankInfo> bankinfo) {
        this.bankinfo = bankinfo;
    }

    public List<OrgTree> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<OrgTree> orgs) {
        this.orgs = orgs;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    /**
     * 公司开票信息
     */
    public class InvoiceInfo extends BaseModel{
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
}
