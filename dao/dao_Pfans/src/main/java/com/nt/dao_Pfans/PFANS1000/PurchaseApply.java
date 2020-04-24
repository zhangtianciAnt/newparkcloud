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
}
