package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "assetinformation")
public class Assetinformation extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 固定资产信息
	 */
    @Id
    @Column(name = "ASSETINFORMATION_ID")
    private String assetinformationid;

//    /**
//     * 件名
//     */
//    @Column(name = "CENTER_ID")
//    private String center_id;

//    /**
//     * 件名
//     */
//    @Column(name = "GROUP_ID")
//    private String group_id;
//
//    /**
//     * 件名
//     */
//    @Column(name = "TEAM_ID")
//    private String team_id;
//
//    /**
//     * 件名
//     */
//    @Column(name = "USER_ID")
//    private String user_id;

    /**
     * 件名
     */
    @Column(name = "FILENAME")
    private String filename;

    /**
     * 概要（目的/效果）
     */
    @Column(name = "SUMMARY")
    private String summary;

    /**
     * 处理方式
     */
    @Column(name = "PROCESSINGMETHOD")
    private String processingmethod;

    /**
     * 出售合同
     */
    @Column(name = "SALECONTRACT")
    private String salecontract;

    /**
     * 合同号
     */
    @Column(name = "CONTRACTNO")
    private String contractno;

    /**
     * 数量
     */
    @Column(name = "NUMBERS")
    private String numbers;

    /**
     * 原值总额
     */
    @Column(name = "TOTALVALUE")
    private String totalvalue;

    /**
     * 净值总额
     */
    @Column(name = "TOTALNETWORTH")
    private String totalnetworth;

    /**
     * 出售报价单
     */
    @Column(name = "SALEQUOTATION")
    private String salequotation;

    /**
     * 不足两份报价单理由
     */
    @Column(name = "REASONSFORQUOTATION")
    private String reasonsforquotation;

    /**
     * 附件
     */
    @Column(name = "UPLOADFILE")
    private String uploadfile;

}
