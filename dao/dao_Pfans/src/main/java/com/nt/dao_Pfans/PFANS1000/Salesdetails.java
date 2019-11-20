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
@Table(name = "salesdetails")
public class Salesdetails extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 出售资产明细
	 */
    @Id
    @Column(name = "SALESDETAILS_ID")
    private String salesdetailsid;

    /**
     * 固定资产名称
     */
    @Column(name = "FIXEDASSETNAME")
    private String fixedassetname;

    /**
     * megas编号
     */
    @Column(name = "MEGASNUMBER")
    private String megasnumber;

    /**
     * 固定资产标签号
     */
    @Column(name = "SETTAGNUMBER")
    private String settagnumber;

    /**
     * 购入时间
     */
    @Column(name = "PURCHASEDATE")
    private Date purchasedate;

    /**
     * 原值
     */
    @Column(name = "ORIGINALVALUE")
    private String originalvalue;

    /**
     * 净值
     */
    @Column(name = "NETWORTH")
    private String networth;

    /**
     * 卖价
     */
    @Column(name = "SELLINGPRICE")
    private String sellingprice;

    /**
     * 损失额
     */
    @Column(name = "LOSS")
    private String loss;

    /**
     * 报废原因
     */
    @Column(name = "SCRAPPING")
    private String scrapping;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

}
