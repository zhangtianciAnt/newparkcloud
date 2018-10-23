package com.nt.dao_Org;

import com.nt.utils.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "orgtree")
/*
组织ID	ORGID
节点ID	NODEID
节点名称	TITLE
节点类型	TYPE
*/
public class OrgTree extends BaseModel {

    private String orgid;
    private String nodeid;
    private String title;
    private String type; //1：公司；2：部门
    private Orgs orgs;

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

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

    public Orgs getOrgs() {
        return orgs;
    }

    public void setOrgs(Orgs orgs) {
        this.orgs = orgs;
    }

    /*
    节点ID	NODEID
    节点名称	TITLE
    节点类型	TYPE
    公司ID	COMPANYID
    公司简称	COMPANYSHORTNAME
    公司英文大写	COMPANYEN
    企业名称	COMPANYNAME
    公司地址	COMPANYADDRESS
    公司法人	COMPANYCORPORATION
    公司执照	UNIFEDSOCIALCREDITCODE
    公司成立时间	ESTABLISH
    部门ID	DEPARTMENTID
    部门名	DEPARTMENTNAME
    */
    public class Orgs extends BaseModel {

        private String nodeid;
        private String title;
        private String type;
        private String departmentid;
        private String departmentname;
        private String companyid;
        private String companyshortname;
        private String companyen;
        private String companyname;
        private String companyaddress;
        private String companycorporation;
        private String unifedsocialcreditcode;
        private Date establish;
        private Invoiceinfo invoiceinfo;
        private Bankinfo bankinfo;

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

        public Invoiceinfo getInvoiceinfo() {
            return invoiceinfo;
        }

        public void setInvoiceinfo(Invoiceinfo invoiceinfo) {
            this.invoiceinfo = invoiceinfo;
        }

        public Bankinfo getBankinfo() {
            return bankinfo;
        }

        public void setBankinfo(Bankinfo bankinfo) {
            this.bankinfo = bankinfo;
        }

        /*
        公司名称	COMPANYNAME
        纳税人识别号	DUTYNUMBER
        地址	COMPANYADDRESS
        电话	PHONE
        银行名称	BANKNAME
        开户行	BANKBRANCH
        账号	BANKNUMBER
        */
        public class Invoiceinfo extends BaseModel {

            private String companyname;
            private String dutynumber;
            private String companyaddress;
            private String phone;
            private String bankname;
            private String bankbranch;
            private String banknumber;

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

        /*
        银行账户ID	BANKACCID
        银行名称	BANKNAME
        开户行	BANKBRANCH
        银行账号	BANKNUMBER
        公司ID	COMPANYID
        */
        public class Bankinfo extends BaseModel {

            private String bankaccid;
            private String bankname;
            private String bankbranch;
            private String banknumber;
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
    }


}


