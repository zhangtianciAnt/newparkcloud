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
@Table(name = "purchaseapply")
public class PurchaseApply extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 千元以下物品购入事前申请ID
     */
    @Id
    @Column(name = "PURCHASEAPPLY_ID")
    private String purchaseapply_id;

    /**
     * 所属センター
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 所属グループ
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 所属チーム
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * 申请人
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 申请日期
     */
    @Column(name = "APPLICATION_DATE")
    private Date application_date;

    /**
     * 预算编码
     */
    @Column(name = "BUDGETUNIT")
    private String budgetunit;
//add-ws-4/17-费用编号添加
    /**
     * 费用编号
     */
    @Column(name = "PURCHASENUMBERS")
    private String purchasenumbers;

//add-ws-4/17-费用编号添加

//add-ws-4/23-总务担当可用选择部门带出预算编码
@Column(name = "GROUP_NAME")
private String group_name;
    //add-ws-4/23-总务担当可用选择部门带出预算编码

//add-ws-4/17-摘要添加
    /**
     * 费用编号
     */
    @Column(name = "REMARKS")
    private String remarks;
//add-ws-4/17-费用编号添加

    //总金额
    @Column(name = "SUMMONEY")
    private String summoney;

    //ADD_FJL_0730 START
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
    //ADD_FJL_0730 END
    //add_fjl_0813
    //余额
    @Column(name = "BALANCEJUDE")
    private String balancejude;
    //add_fjl_0813
    //add_fjl_0824
    //关联决裁的扣除顺序
//    @Column(name = "JUDEINDEX")
//    private Integer judeindex;
//    //扣除费用
//    @Column(name = "SPENDJUDE")
//    private Float spendjude;
    //add_fjl_0824
}
