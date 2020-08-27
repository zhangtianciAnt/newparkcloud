package com.nt.dao_Pfans.PFANS3000;


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
@Table(name = "purchase")
public class Purchase  extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 购买申请表ID
     */
    @Id
    @Column(name = "PURCHASE_ID")
    private String purchase_id;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "CENTER_ID")
    private String center_id;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "TEAM_ID")
    private String team_id;

    @Column(name = "APPLICATION_DATE")
    private Date application_date;

    /**
     * 内线号
     */
    @Column(name = "LINENUMBER")
    private String linenumber;

    /**
     * 放置场所
     */
    @Column(name = "SETPLACE")
    private String setplace;

    /**
     * 管理者
     */
    @Column(name = "CONTROLLER")
    private String controller;

    /**
     * 使用者
     */
    @Column(name = "USERNAME")
    private String username;

    //使用期限
    @Column(name = "USERTIME")
    private String usertime;

    /**
     * 预算号
     */
    @Column(name = "BUDGETNUMBER")
    private String budgetnumber;

    /**
     * 事业计划
     */
    @Column(name = "CAREERPLAN")
    private String careerplan;

    /**
     * 事业计划类型
     */
    @Column(name = "BUSINESSPLANTYPE")
    private String businessplantype;

    /**
     * 分类类型
     */
    @Column(name = "CLASSIFICATIONTYPE")
    private String classificationtype;

    /**
     * 事业计划余额
     */
    @Column(name = "BUSINESSPLANBALANCE")
    private String businessplanbalance;

    /**
     * 事业计划金额
     */
    @Column(name = "BUSINESSPLANAMOUNT")
    private String businessplanamount;

    /**
     * 购入目的
     */
    @Column(name = "PURCHASEPURPOSE")
    private String purchasepurpose;

    /**
     * 采购明细
     */
    @Column(name = "PROCUREMENTDETAILS")
    private String procurementdetails;

    /**
     * 采购项目
     */
    @Column(name = "PROCUREMENTPROJECT")
    private String procurementproject;

    /**
     * 固定(簿外)资产编号
     */
    @Column(name = "FIXEDASSETSNO")
    private String fixedassetsno;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 品牌名
     */
    @Column(name = "BRANDNAME")
    private String brandname;

    /**
     * 型号
     */
    @Column(name = "MODEL")
    private String model;

    /**
     * 設備URL
     */
    @Column(name = "EQUIPMENTURL")
    private String equipmenturl;

    /**
     * 数量
     */
    @Column(name = "QUANTITY")
    private String quantity;

    /**
     * 单价
     */
    @Column(name = "UNITPRICE")
    private String unitprice;

    /**
     * 总金额
     */
    @Column(name = "TOTALAMOUNT")
    private String totalamount;

    /**
     * 入库日
     */
    @Column(name = "STORAGEDATE")
    private Date storagedate;

    /**
     * 领取日
     */
    @Column(name = "COLLECTIONDAY")
    private Date collectionday;

    /**
     * 领取者
     */
    @Column(name = "RECIPIENTS")
    private String recipients;

    /**
     * 精算日
     */
    @Column(name = "ACTUARIALDATE")
    private Date actuarialdate;

    /**
     * 精算金額
     */
    @Column(name = "ACTUARIALAMOUNT")
    private String actuarialamount;

    /**
     * 是否受理
     */
    @Column(name = "ACCEPT")
    private String accept;

    /**
     * 受理状态
     */
    @Column(name = "ACCEPTSTATUS")
    private String acceptstatus;

    /**
     * 完成日期
     */
    @Column(name = "FINDATE")
    private Date findate;

    /**
     * 拒绝理由
     */
    @Column(name = "REFUSEREASON")
    private String refusereason;
//add-ws-4/28-采购编号添加
    /**
     * 采购编号
     */
    @Column(name = "PURNUMBERS")
    private String purnumbers;
    //add-ws-4/28-采购编号添加
//add-ws-6/16-禅道137
    /**
     * 项目id
     */
    @Column(name = "PROJECT_ID")
    private String project_id;
//add-ws-6/16-禅道137

    //add ccm 0720
    //报价单
    @Column(name = "SALEQUOTATION")
    private String salequotation;
    //不足俩份理由
    @Column(name = "REASONSFORQUOTATION")
    private String reasonsforquotation;
    //附件
    @Column(name = "UPLOADFILE")
    private String uploadfile;
    //预算不足理由
    @Column(name = "YUSUANBUZUCASE")
    private String yusuanbuzu;
    //暂借款ID
    @Column(name = "LOANAPPLICATION_ID")
    private String loanapplication_id;
    //暂借款编号
    @Column(name = "LOANAPNO")
    private String loanapno;
    //精算报销编号
    @Column(name = "INVOICENO")
    private String invoiceno;
    //精算ID
    @Column(name = "PUBLICEXPENSE_ID")
    private String publicexpense_id;
    //add ccm 0720
    //add_fjl_0813
    //余额
    @Column(name = "BALANCEJUDE")
    private String balancejude;
    //add_fjl_0813
    //add_fjl_0819
    //关联决裁的扣除顺序
//    @Column(name = "JUDEINDEX")
//    private Integer judeindex;
//    //扣除费用
//    @Column(name = "SPENDJUDE")
//    private Float spendjude;
    //add_fjl_0819
}
