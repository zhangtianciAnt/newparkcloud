package com.nt.dao_AOCHUAN.AOCHUAN6000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reimbursement")
public class Reimbursement extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //主键
    @Id
    @Column(name = "REIMBURSEMENT_ID")
    private String reimbursement_id;

    //费用单编号
    @Column(name = "REIMBURSEMENT_NO")
    private String reimbursement_no;

    //部门
    @Column(name = "SECTION")
    private String section;

    //申请人
    @Column(name = "APPLICANT")
    private String applicant;

    //事由
    @Column(name = "MATTER")
    private String matter;

    //申请时间
    @Column(name = "APPLICATION_DATE")
    private String application_date;

    //费用类型
    @Column(name = "CHARGE_TYPE")
    private String charge_type;

    //陪同人员
    @Column(name = "ACCOMPANYING_PERSON")
    private String accompanying_person;

    //出差日期
    @Column(name = "BUSINESS_TRIP_DATE")
    private String business_trip_date;

    //行程
    @Column(name = "JOURNEY")
    private String journey;
}
