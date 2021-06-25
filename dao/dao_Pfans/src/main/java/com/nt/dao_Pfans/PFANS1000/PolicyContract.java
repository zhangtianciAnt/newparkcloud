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
@Table(name = "policycontract")
public class PolicyContract extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "POLICYCONTRACT_ID")
    private String policycontract_id;


    /**
     * 合同号
     */
    @Column(name = "POLICYNUMBERS")
    private String policynumbers;

    /**
     * 起案者
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 委托元
     */
    @Column(name = "COMMISSION")
    private String commission;

    /**
     * 起案金额
     */
    @Column(name = "AMOUNTCASE")
    private String amountcase;

    /**
     * 修改后金额
     */
    @Column(name = "MODIFIEDAMOUNT")
    private String modifiedamount;

    /**
     * 实际核销金额
     */
    @Column(name = "NEWAMOUNTCASE")
    private String newamountcase;

    /**
     * 方针可用金额
     */
    @Column(name = "AVBLEAMOUNT")
    private String avbleamount;

    /**
     * 周期
     */
    @Column(name = "CYCLE")
    private String cycle;

    /**
     * 备注
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 外协公司
     */
    @Column(name = "OUTSOURCINGCOMPANY")
    private String outsourcingcompany;

    /**
     * 通货形式
     */
    @Column(name = "CURRENCY")
    private String currency;

    /**
     * 起案日
     */
    @Column(name = "APPLICATIONDATE")
    private Date applicationdate;


    /**
     * 附件
     */
    @Column(name = "UPLOADFILE")
    private String uploadfile;

    /**
     * 决裁关联请求金额合计
     */
    @Column(name = "SUMMONET")
    private String summonet;


    /**
     * 觉书信息
     */
    @Column(name = "INFORMATION")
    private String information;
    /**
     * 觉书信息显示隐藏判断
     */
    @Column(name = "TYPE")
    private String type;

    @Column(name = "YEARSS")
    private String yearss;
}
