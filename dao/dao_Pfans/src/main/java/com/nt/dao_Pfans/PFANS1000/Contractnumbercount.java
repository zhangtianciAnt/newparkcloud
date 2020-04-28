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
@Table(name = "contractnumbercount")
public class Contractnumbercount extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 契约番号回数
     */
    @Id
    @Column(name = "CONTRACTNUMBERCOUNT_ID")
    private String contractnumbercount_id;

    /**
     * 契約書番号
     */
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    /**
     * 請求方式
     */
    @Column(name = "CLAIMTYPE")
    private String claimtype;

    /**
     * 納品予定日
     */
    @Column(name = "DELIVERYDATE")
    private Date deliverydate;

    /**
     * 検収完了日
     */
    @Column(name = "COMPLETIONDATE")
    private Date completiondate;
    //add-ws-添加纳品做成日和出荷判定实施者
    @Column(name = "DELIVERYFINSHDATE")
    private Date deliveryfinshdate;

    @Column(name = "LOADINGJUDGE")
    private String loadingjudge;
    //add-ws-添加纳品做成日和出荷判定实施者
    /**
     * 請求日
     */
    @Column(name = "CLAIMDATE")
    private Date claimdate;

    /**
     * 請求金額
     */
    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    /**
     * 通货单位
     */
    @Column(name = "CURRENCYPOSITION")
    private String currencyposition;

    /**
     * 支払日
     */
    @Column(name = "SUPPORTDATE")
    private Date supportdate;

    /**
     * 区分
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    @OrderBy("ASC")
    private Integer rowindex;

    /**
     * 請求番号
     */
    @Column(name = "CLAIMNUMBER")
    private String claimnumber;

    /**
     * PJ起案 委托元为内采时
     */
    @Column(name = "COMPANYPROJECTSID")
    private String companyprojectsid;

//    ADD_FJL
    /**
     * 备考
     */
    @Column(name = "REMARKS")
    private String remarksqh;

    /**
     * 请求书特殊备考
     */
    @Column(name = "QINGREMARKS")
    private String qingremarksqh;

    /**
     * 请求推进状况_对象
     */
    @Column(name = "CLAIM")
    private String claimqh;

    /**
     * 请求推进状况_状况
     */
    @Column(name = "CLAIMCONDITION")
    private String claimconditionqh;

    /**
     * 纳品推进状况_对象
     */
    @Column(name = "DELIVERY")
    private String deliveryqh;

    /**
     * 纳品推进状况_状况
     */
    @Column(name = "DELIVERYCONDITION")
    private String deliveryconditionqh;

    /**
     * 請求期间
     */
    @Column(name = "CLAIMDATETIME")
    private String claimdatetimeqh;

    /**
     * 资金回收状况
     */
    @Column(name = "RECOVERYSTATUS")
    private String recoverystatus;

    /**
     * 回收日期
     */
    @Column(name = "RECOVERYDATE")
    private Date recoverydate;
//    ADD_FJL

}
