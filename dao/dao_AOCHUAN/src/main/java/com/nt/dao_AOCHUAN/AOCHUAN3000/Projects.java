package com.nt.dao_AOCHUAN.AOCHUAN3000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projects")
public class Projects extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PROJECTS_ID")
    private String projects_id;

    @Column(name = "PRODUCTS_ID")
    private String products_id;

    @Column(name = "SUPPLIER_ID")
    private String supplier_id;

    //项目名称
    @Column(name = "PRODUCT_NM")
    private String product_nm;

    //供应商
    @Column(name = "SUPPLIER")
    private String supplier;

    //客户
    @Column(name = "CUSTOMER")
    private String customer;

    //药典
    @Column(name = "PHARMACOPOEIA")
    private String pharmacopoeia;

    //原研InterView
    @Column(name = "ORIGINALRESEARCH_INTERVIEW")
    private String originalresearch_interview;

    //审查报告书
    @Column(name = "EXAMINATION_REPORT")
    private String examination_report;

    //申请资料概要
    @Column(name = "APPLICATION_MATERIALS")
    private String application_materials;

    //保密协议
    @Column(name = "CONFIDENTIALITY_AGREEMENT")
    private String confidentiality_agreement;

    //工厂报价
    @Column(name = "FACTORY_QUOTATION")
    private String factory_quotation;

    //给客户
    @Column(name = "TO_CUSTOMER")
    private String to_customer;

    //小试COA
    @Column(name = "SMALL_SCALETEST_COA")
    private String small_scaletest_coa;

    //中式COA
    @Column(name = "MEDIUM_SCALETEST_COA")
    private String medium_scaletest_coa;

    //专利确认信息
    @Column(name = "PATENT_CONFIRMATION_INFORMATION")
    private String patent_confirmation_information;

    //MSDS
    @Column(name = "MSDS")
    private String msds;

    //TSE/BSE声明
    @Column(name = "STATEMENT_TSE_BSE")
    private String statement_tse_bse;

    //金属元素检测
    @Column(name = "METALELEMENT_DETECTION")
    private String metalelement_detection;

    //ROS
    @Column(name = "ROS")
    private String ros;

    //杂志列表
    @Column(name = "MAGAZINE_LIST")
    private String magazine_list;

    //SST
    @Column(name = "SST")
    private String sst;

    //MOA
    @Column(name = "MOA")
    private String moa;

    //稳定性数据
    @Column(name = "STABILITY_DATA")
    private String stability_data;

    //光的稳定性检测
    @Column(name = "STABILITY_DETECTION_OF_LIGHT")
    private String stability_detection_of_light;

    //XRD
    @Column(name = "XRD")
    private String xrd;

    //溶解性状试验
    @Column(name = "SOLUBILITY_TEST")
    private String solubility_test;

    //样品发送
    @Column(name = "SAMPLE_SENDING")
    private String sample_sending;

    //3PV COA
    @Column(name = "THRIDPVCOA")
    private String thridpvcoa;

    //客户希望DMF注册时间
    @Column(name = "DMFCUSTOMERSCHEDULEDREGISTRATIONTIME")
    private String dmfcustomerscheduledregistrationtime;

    //客户官方审计时间
    @Column(name = "CLIENTOFFICIALAUDITTIME")
    private String clientofficialaudittime;

    //GE药上市时间
    @Column(name = "PILLSLISTINGTIME")
    private String pillslistingtime;
}
