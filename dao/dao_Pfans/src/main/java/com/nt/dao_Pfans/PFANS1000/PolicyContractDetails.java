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
@Table(name = "policycontractdetails")
public class PolicyContractDetails extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    @Column(name = "POLICYCONTRACTDETAILS_ID")
    private String policycontractdetails_id;


    /**
     * 外键
     */
    @Id
    @Column(name = "POLICYCONTRACT_ID")
    private String policycontract_id;

    /**
     * 觉书回数
     */
    @Id
    @Column(name = "INVOICENUMBER")
    private String invoicenumber;

    /**
     * 请求金额
     */
    @Id
    @Column(name = "MONEY")
    private String money;

    /**
     * 备注
     */
    @Id
    @Column(name = "REMARK")
    private String remark;



}
