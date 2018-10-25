package com.nt.dao_Org;

import com.nt.utils.AuthConstants;
import com.nt.utils.BaseModel;
import com.nt.utils.TokenModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: OrgTree
 * @Description: 组织机构
 * @Author: wenchao
 * @CreateDate: 2018/10/25
 * @UpdateUser: wenchao
 * @UpdateDate: 2018/10/25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Document(collection = "orgtree")
@Data
public class OrgTree extends BaseModel {
    // region properties
    /**
     * 数据主键ID
     */
    private String _id;
    /**
    组织ID	ORGID
    */
    private String orgid;
    /**
    节点ID	NODEID
    */
    private String nodeid;
    /**
    节点名称	TITLE
    */
    private String title;
    /**
    节点类型	TYPE
    */
    private String type; //1：公司；2：部门
    /**
    组织单元	ORGS
    */
    private List<Orgs> orgs;
    /**
    下级组织机构	ORGTREES
    */
    private List<OrgTree> orgtrees;

    // region 组织单元
    /**
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
    @Data
    public static class Orgs extends BaseModel {

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
        private List<Bankinfo> bankinfo;

        // region 公司开票信息
        /*
        公司名称	COMPANYNAME
        纳税人识别号	DUTYNUMBER
        地址	COMPANYADDRESS
        电话	PHONE
        银行名称	BANKNAME
        开户行	BANKBRANCH
        账号	BANKNUMBER
        */
        @Data
        public static class Invoiceinfo extends BaseModel {

            private String companyname;
            private String dutynumber;
            private String companyaddress;
            private String phone;
            private String bankname;
            private String bankbranch;
            private String banknumber;
        }
        // endregion

        // region 公司账户信息
        /*
        银行账户ID	BANKACCID
        银行名称	BANKNAME
        开户行	BANKBRANCH
        银行账号	BANKNUMBER
        公司ID	COMPANYID
        */
        @Data
        public static class Bankinfo extends BaseModel {

            private String bankaccid;
            private String bankname;
            private String bankbranch;
            private String banknumber;
            private String companyid;
        }
        // endregion
    }
    // endregion
    // endregion

    // region method
    /**
     * @方法名：preInsert
     * @描述：数据插入前，基础字段数据更新
     * @创建日期：2018/10/25
     * @作者：SKAIXX
     * @参数：[tokenModel]
     * @返回值：void
     */
    @Override
    public void preInsert(TokenModel tokenModel){
        for (Orgs tmp: this.orgs) {
            tmp.setCreateby(tokenModel.getUserId());
            tmp.setCreateon(new Date());
            tmp.setOwner(tokenModel.getUserId());
            tmp.setTenantid(tokenModel.getTenantId());
            tmp.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        }
        this.setCreateby(tokenModel.getUserId());
        this.setCreateon(new Date());
        this.setOwner(tokenModel.getUserId());
        this.setTenantid(tokenModel.getTenantId());
        this.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        if (orgtrees != null && orgtrees.size() > 0) {
            for (OrgTree tmp : this.orgtrees) {
                preInsert(tokenModel);
            }
        }
    }

    /**
     * @方法名：preUpdate
     * @描述：数据更新前，基础字段数据更新
     * @创建日期：2018/10/25
     * @作者：SKAIXX
     * @参数：[tokenModel]
     * @返回值：void
     */
    @Override
    public void preUpdate(TokenModel tokenModel){
        for (Orgs tmp: this.orgs) {
            tmp.setModifyby(tokenModel.getUserId());
            tmp.setModifyby(tokenModel.getUserId());
            tmp.setModifyon(new Date());
        }
        this.setModifyby(tokenModel.getUserId());
        this.setModifyon(new Date());
    }
    // endregion
}


