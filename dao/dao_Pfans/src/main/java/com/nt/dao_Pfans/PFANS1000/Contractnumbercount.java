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
    private Integer rowindex;
}
