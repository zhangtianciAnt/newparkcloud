package com.nt.dao_AOCHUAN.AOCHUAN6000;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reimbursement_detail")
public class ReimbursementDetail {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //主键
    @Id
    @Column(name = "REIMBURSEMENT_DETAIL_ID")
    private String reimbursement_detail_id;

    //费用明细日期
    @Column(name = "REIMBURSEMENT_DETAIL_DATE")
    private String reimbursement_detail_date;

    //当日事由
    @Column(name = "DAY_MATTER")
    private String day_matter;

    //地点
    @Column(name = "SITE")
    private String site;

    //项目
    @Column(name = "OBJECT_TYPE")
    private String object_type;

    //摘要
    @Column(name = "ABSTRACT_CONTENT")
    private String abstract_content;

    //人数
    @Column(name = "PERSONS")
    private String persons;

    //预算金额
    @Column(name = "BUDGET_AMOUNT")
    private String budget_amount;

    //备注说明
    @Column(name = "CONTENT")
    private String content;

    //实际金额
    @Column(name = "ACTUAL_AMOUNT")
    private String actual_amount;
}
