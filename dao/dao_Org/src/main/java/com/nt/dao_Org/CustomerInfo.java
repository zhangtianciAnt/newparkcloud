package com.nt.dao_Org;

import com.nt.utils.AuthConstants;
import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: CustomerInfo
 * @Description: 客户信息
 * @Author: SKAIXX
 * @CreateDate: 2018/10/25
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/10/25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

@Document(collection = "customerinfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfo extends BaseModel {
    // region properties
    /**
     * 数据主键ID
     */
    private String _id;
    /**
     * 客户ID
     */
    private String customerid;
    /**
     * 用户ID
     */
    private String userid;
    /**
     * 客户类型 0:企业;1:个人
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

    // region 个人信息
    @Data
    public static class UserInfo {

        /**
         * 数据主键ID
         */
        private String _id;
        /**
         * 姓名
         */
        private String customername;
        /**
         * 手机
         */
        private String mobilenumber;
        /**
         * 邮箱
         */
        private String email;
        /**
         * 身份证号
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
    }
    // endregion

    // region 企业信息
    @Data
    public static class CompanyInfo {

        /**
         * 数据主键ID
         */
        private String _id;
        /**
         * 企业名称
         */
        private String companyname;
        /**
         * 行业分类
         */
        private String industrytype;
        /**
         * 来源渠道
         */
        private String source;
        /**
         * 标签
         */
        private String label;
        /**
         * 客户阶段
         */
        private String customerstage;

        /**
         * 租户信息
         */
        private TenantInfo tenantinfo;

        /**
         * 联系人
         */
        private List<ContactInfo> contactinfo;

        /**
         * 开票信息
         */
        private List<InvoiceInfo> invoiceinfo;

        /**
         * 工商信息
         */
        private IndustryInfo industryinfo;

        // region 租户信息
        @Data
        public static class TenantInfo {

            /**
             * 数据主键ID
             */
            private String _id;
            /**
             * 企业LOGO
             */
            private String companylogo;
            /**
             * 统一社会信息代码
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
             * 公司成立时间
             */
            private Date establish;
            /**
             * 门户网址
             */
            private String portalurl;
        }
        // endregion

        // region 联系人信息
        @Data
        public static class ContactInfo extends BaseModel {

            /**
             * 数据主键ID
             */
            private String _id;
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
        }
        // endregion

        // region 开票信息
        @Data
        public static class InvoiceInfo {

            /**
             * 数据主键ID
             */
            private String _id;
            /**
             * 开户行
             */
            private String bankbranch;
            /**
             * 账号
             */
            private String banknumber;
            /**
             * 电话
             */
            private String tel;
            /**
             * 纳税人识别号
             */
            private String dutynumber;
            /**
             * 开票地址
             */
            private String billingaddress;
        }
        // endregion

        // region 工商信息
        @Data
        public static class IndustryInfo {

            /**
             * 数据主键ID
             */
            private String _id;
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
            private String timeofestablishment;
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
            private String dateofapproval;
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
        this.setCreateby(tokenModel.getUserId());
        this.setCreateon(new Date());
        this.setOwner(tokenModel.getUserId());
        this.setTenantid(tokenModel.getTenantId());
        this.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        if (this.companyinfo != null && this.companyinfo.getContactinfo() != null && this.companyinfo.getContactinfo().size() > 0) {
            for (CompanyInfo.ContactInfo ci : this.companyinfo.getContactinfo()) {
                ci.preInsert(tokenModel);
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
        this.setModifyby(tokenModel.getUserId());
        this.setModifyon(new Date());
        if (this.companyinfo != null && this.companyinfo.getContactinfo() != null && this.companyinfo.getContactinfo().size() > 0) {
            for (CompanyInfo.ContactInfo ci : this.companyinfo.getContactinfo()) {
                ci.preUpdate(tokenModel);
            }
        }
    }
    // endregion
}

