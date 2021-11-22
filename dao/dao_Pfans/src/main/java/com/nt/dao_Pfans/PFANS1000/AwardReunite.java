package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "awardreunite")
public class AwardReunite extends BaseModel {

    /**
     * 决裁书复合合同  PSDCD_PFANS_20210525_XQ_054 复合合同决裁书分配金额可修改 ztc
     */
    @Id
    @Column(name = "AWARDREUNITE_ID")
    private String awardreunite_id;

    /**
     * 合同号
     */
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    /**
     * 纳品回数
     */
    @Column(name = "CLAIMTYPE")
    private String claimtype;

    /**
     * 部门
     */
    @Column(name = "DEPARTMENT")
    private String department;

    /**
     * 真实部门（用于计算）
     */
    @Column(name = "REALDEPARTMENT")
    private String realdepartment;

    /**
     * 纳品预定日
     */
    @Column(name = "DELIVERYDATE")
    private Date deliverydate;

    /**
     * 验收完了日
     */
    @Column(name = "COMPLETIONDATE")
    private Date completiondate;

    /**
     * 请求日
     */
    @Column(name = "CLAIMDATE")
    private Date claimdate;

    /**
     * 支付日
     */
    @Column(name = "SUPPORTDATE")
    private Date supportdate;

    /**
     * 请求金额
     */
    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    /**
     * 分配金额
     */
    @Column(name = "DISTRIAMOUNT")
    private String distriamount;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    @OrderBy("ASC")
    private Integer rowindex;
}
