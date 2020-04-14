package com.nt.dao_AOCHUAN.AOCHUAN3000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    //产品名称
    @Column(name = "PROJECT_NM")
    private String PROJECT_NM;

    //供应商
    @Column(name = "PROVIDER")
    private String PROVIDER;

    //客户
    @Column(name = "CUSTOMER")
    private String CUSTOMER;

    //药典
    @Column(name = "PHARMACOPOEIA")
    private String PHARMACOPOEIA;

    //原研InterView
    @Column(name = "ORIGINALRESEARCH_INTERVIEW")
    private String ORIGINALRESEARCH_INTERVIEW;

    //审查报告书
    @Column(name = "EXAMINATION_REPORT")
    private String EXAMINATION_REPORT;

    //申请资料概要
    @Column(name = "APPLICATION_MATERIALS")
    private String APPLICATION_MATERIALS;

    //保密协议
    @Column(name = "CONFIDENTIALITY_AGREEMENT")
    private String CONFIDENTIALITY_AGREEMENT;

    //工厂报价
    @Column(name = "FACTORY_QUOTATION")
    private String FACTORY_QUOTATION;

    //给客户
    @Column(name = "TO_CUSTOMER")
    private String TO_CUSTOMER;

    //小试COA
    @Column(name = "SMALL_SCALETEST_COA")
    private String SMALL_SCALETEST_COA;

    //中式COA
    @Column(name = "MEDIUM_SCALETEST_COA")
    private String MEDIUM_SCALETEST_COA;

    //专利确认信息
    @Column(name = "PATENT_CONFIRMATION_INFORMATION")
    private String PATENT_CONFIRMATION_INFORMATION;

    //MSDS
    @Column(name = "MSDS")
    private String MSDS;

    //TSE/BSE声明
    @Column(name = "STATEMENT_TSE_BSE")
    private String STATEMENT_TSE_BSE;

    //金属元素检测
    @Column(name = "METALELEMENT_DETECTION")
    private String METALELEMENT_DETECTION;

    //ROS
    @Column(name = "ROS")
    private String ROS;

    //杂志列表
    @Column(name = "MAGAZINE_LIST")
    private String MAGAZINE_LIST;

    //SST
    @Column(name = "SST")
    private String SST;

    //MOA
    @Column(name = "MOA")
    private String MOA;

    //稳定性数据
    @Column(name = "STABILITY_DATA")
    private String STABILITY_DATA;

    //光的稳定性检测
    @Column(name = "STABILITY_DETECTION_OF_LIGHT")
    private String STABILITY_DETECTION_OF_LIGHT;

    //XRD
    @Column(name = "XRD")
    private String XRD;

    //溶解性状试验
    @Column(name = "SOLUBILITY_TEST")
    private String SOLUBILITY_TEST;

    //样品发送
    @Column(name = "SAMPLE_SENDING")
    private String SAMPLE_SENDING;
}
