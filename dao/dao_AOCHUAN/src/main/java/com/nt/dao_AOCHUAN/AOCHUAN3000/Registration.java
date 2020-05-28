package com.nt.dao_AOCHUAN.AOCHUAN3000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registration")
public class Registration extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //主键
    @Id
    @Column(name = "REG_ID")
    private String reg_id;

    //产品id
    @Column(name = "PRODUCT_ID")
    private String product_id;

    //供应商
    @Column(name = "SUPPLIER_ID")
    private String supplier_id;

    //沟通：生产商
    @Column(name = "COMM_PRODUCERS")
    private String comm_producers;

    //沟通：报价编号
    @Column(name = "QUOTATION_NO")
    private String quotation_no;

    //沟通：GMP证书
    @Column(name = "CERTIFICATE_GMP")
    private String certificate_gmp;

    //沟通：申请日期
    @Column(name = "APPLICATION_DATE")
    private Date application_date;

    //沟通：补充日期
    @Column(name = "COMM_SUPPLEMENT_DATE")
    private Date comm_supplement_date;

    //沟通：预期审批日期
    @Column(name = "EXPAPPROVAL_DATE")
    private Date expapproval_date;

    //沟通：审批/撤回日期
    @Column(name = "APPROVAL_RECALL_DATE")
    private Date approval_recall_date;

    //沟通：MFDS担当
    @Column(name = "MFDS_OWNER")
    private String mfds_owner;

    //沟通：区分
    @Column(name = "DIFF")
    private String diff;

    //沟通：规格/审计
    @Column(name = "SPFC_AUDIT")
    private String spfc_audit;

    //沟通：备注
    @Column(name = "COMM_REMARKS")
    private String comm_remarks;

    //沟通：工厂进展
    @Column(name = "FACTORY_PROGRESS")
    private String factory_progress;

    //沟通：奥川进展
    @Column(name = "AUTRAN_PROGRESS")
    private String autran_progress;

    //沟通：MFDS进展
    @Column(name = "MFDS_PROGRESS")
    private String mfds_progress;

    //维护：注册编号
    @Column(name = "REG_NO")
    private String reg_no;

    //维护：生产商
    @Column(name = "MAINT_PRODUCERS")
    private String maint_producers;

    //维护：现场审计
    @Column(name = "ONSITE_AUDIT")
    private String onsite_audit;

    //维护：生产商地址
    @Column(name = "PRODUCERS_ADDRESS")
    private String producers_address;

    //维护：国家
    @Column(name = "COUNTRY")
    private String country;

    //维护：补充日期
    @Column(name = "MAINT_SUPPLEMENT_DATE")
    private Date maint_supplement_date;

    //维护：审批日期
    @Column(name = "APPROVAL_DATE")
    private Date approval_date;

    //维护：贮藏条件
    @Column(name = "STORAGE_CONDITION")
    private String storage_condition;

    //维护：有效期/复验期
    @Column(name = "VALIDITY_REINSPECTION")
    private String validity_reinspection;

    //维护：规格
    @Column(name = "SPECIFICATIONS")
    private String specifications;

    //维护：持有人/授权
    @Column(name = "HOLDER_AUTHORIZATION")
    private String holder_authorization;

    //维护：GMP证书
    @Column(name = "MAINT_CERTIFICATE_GMP")
    private String maint_certificate_gmp;

    //维护：备注
    @Column(name = "MAINT_REMARKS")
    private String maint_remarks;



}
