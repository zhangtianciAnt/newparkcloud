package com.nt.dao_Pfans.PFANS6000;

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
@Table(name = "entrustsupport")
public class EntrustSupport extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     * */
    @Id
    @Column(name = "ENTRUSTSUPPORT_ID")
    private String entrustsupport_id;

    /**
     * 部门ID
     * */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 部门简称
     * */
    @Column(name = "DEPARTMENT")
    private String department;

    /**
     * 部门名称
     * */
    @Column(name = "DEPLOYMENT")
    private String deployment;

    /**
     * 年月
     * */
    @Column(name = "DATES")
    private String dates;

    /**
     * 合同号
     * */
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    /**
     * 项目名
     * */
    @Column(name = "CONJAPANESE")
    private String conjapanese;

    /**
     * 外注公司
     * */
    @Column(name = "CUSTOJAPANESE")
    private String custojapanese;

    /**
     * 请求金额
     * */
    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    /**
     * 纳品日期
     * */
    @Column(name = "DELIVERYDATE")
    private Date deliverydate;

    /**
     * 验收日期
     * */
    @Column(name = "COMPLETIONDATE")
    private Date completiondate;

    /**
     * 请求日期
     * */
    @Column(name = "CLAIMDATE")
    private Date claimdate;

    /**
     * 支付日期
     * */
    @Column(name = "SUPPORTDATE")
    private Date supportdate;

    /**
     * 处理状态（0未处理）
     * */
    @Column(name = "PROCESSING")
    private String processing;

    /**
     * 行
     * */
    @Column(name = "ROWINDEX")
    private String rowindex;

    /**
     * 担当着
     * */
    @Column(name = "UNDERTAKER")
    private String undertaker;

    /**
     * 体制
     * */
    @Column(name = "PROJECTNUMBER")
    private String projectnumber;


}
