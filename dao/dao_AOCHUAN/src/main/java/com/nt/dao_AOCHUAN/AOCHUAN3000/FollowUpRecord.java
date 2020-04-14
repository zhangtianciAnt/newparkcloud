package com.nt.dao_AOCHUAN.AOCHUAN3000;

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
@Table(name = "followuprecord")
public class FollowUpRecord extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "FOLLOWUPRECORD_ID")
    private String followuprecord_id;

    //产品名称
    @Column(name = "PRODUCT_NM")
    private String product_nm;

    //供应商
    @Column(name = "PROVIDER")
    private String provider;

    //销售：客户
    @Column(name = "SAL_CUSTOMER")
    private String sal_customer;

    //销售：时间
    @Column(name = "SAL_DATE")
    private Date sal_date;

    //销售：事件
    @Column(name = "SAL_EVENT")
    private String sal_event;

    //销售：内容
    @Column(name = "SAL_CONTENT")
    private String sal_content;

    //采购：时间
    @Column(name = "PROC_DATE")
    private Date proc_date;

    //采购：答复
    @Column(name = "PROC_ANSWER")
    private String proc_answer;
}
