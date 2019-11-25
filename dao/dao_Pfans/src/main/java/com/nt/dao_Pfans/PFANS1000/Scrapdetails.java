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
@Table(name = "scrapdetails")
public class Scrapdetails extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 报废资产明细
	 */
    @Id
    @Column(name = "SCRAPDETAILS_ID")
    private String scrapdetailsid;

    @Id
    @Column(name = "ASSETINFORMATION_ID")
    private String assetinformationid;


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
     * 使用年限
     */
    @Column(name = "YEARSOFUSE")
    private String yearsofuse;

    /**
     * 净值
     */
    @Column(name = "NETWORTH")
    private String networth;

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

    /**
     * 排序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
