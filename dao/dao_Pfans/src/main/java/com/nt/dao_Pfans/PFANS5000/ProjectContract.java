package com.nt.dao_Pfans.PFANS5000;

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
@Table(name = "projectcontract")
public class ProjectContract extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 项目合同
     */
    @Id
    @Column(name = "PROJECTCONTRACT_ID")
    private String projectcontract_id;

    /**
     * 所属公司项目
     */
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    /**
     * 合同
     */
    @Column(name = "CONTRACT")
    private String contract;

    /**
     * テーマ
     */
    @Column(name = "THEME")
    private String theme;

    //add-ws-合同关联项目，分配金额
    @Column(name = "CONTRACTREQUESTAMOUNT")
    private String contractrequestamount;

    @Column(name = "CONTRACTAMOUNT")
    private String contractamount;
    //add-ws-合同关联项目，分配金额
    /**
     * 工时
     */
    @Column(name = "WORKINGHOURS")
    private String workinghours;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;
    // add-ws-6/9-禅道任务
    @Column(name = "DELIVERYFINSHDATE")
    private Date deliveryfinshdate;

    @Column(name = "CLAIMTYPE")
    private String claimtype;
    // add-ws-6/9-禅道任务
}
