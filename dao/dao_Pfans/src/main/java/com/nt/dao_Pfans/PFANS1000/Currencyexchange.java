package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "currencyexchange")
public class Currencyexchange extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
     * 外币兑换
     */
    @Id
    @Column(name = "CURRENCYEXCHANGEID")
    private String currencyexchangeid;

    @Column(name = "EVECTION_ID")
    private String evectionid;

    /**
     * 币种
     */
    @Column(name = "CURRENCY")
    private String currency;

    /**
     * 金额
     */
    @Column(name = "AMOUNT")
    private String amount;

    /**
     * 兑换汇率
     */
    @Column(name = "EXCHANGERATE")
    private String exchangerate;

    /**
     * 兑换RMB金额
     */
    @Column(name = "EXCHANGERMB")
    private String exchangermb;

    /**
     * 会社用外币汇率
     */
    @Column(name = "CURRENCYEXCHANGERATE")
    private String currencyexchangerate;

    /**
     * 会社用外币汇率
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
