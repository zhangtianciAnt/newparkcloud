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
@Table(name = "contract")
public class Contract extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 契約表
     */
    @Id
    @Column(name = "CONTRACT_ID")
    private String contract_id;

    /**
     * 契約番号
     */
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    /**
     * 契約種類
     */
    @Column(name = "CONTRACTTYPE")
    private String contracttype;

    /**
     * 委託元（和文）
     */
    @Column(name = "DEPOSITJAPANESE")
    private String depositjapanese;

    /**
     * 委託元（中文）
     */
    @Column(name = "DEPOSITCHINESE")
    private String depositchinese;

    /**
     * 委託元場所（和文）
     */
    @Column(name = "PRPLACEJAPANESE")
    private String prplacejapanese;

    /**
     * 委託元場所（中文）
     */
    @Column(name = "PRPLACECHINESE")
    private String prplacechinese;

    /**
     * 開発部署
     */
    @Column(name = "DEPLOYMENT")
    private String deployment;

    /**
     * PJ名（和文）
     */
    @Column(name = "PJNAMEJAPANESE")
    private String pjnamejapanese;

    /**
     * PJ名（中文）
     */
    @Column(name = "PJNAMECHINESE")
    private String pjnamechinese;

    /**
     * 開発開始日
     */
    @Column(name = "OPENINGDATE")
    private Date openingdate;

    /**
     * 開発満了日
     */
    @Column(name = "ENDDATE")
    private Date enddate;

    /*
     * 通貨形式
     */
    @Column(name = "CURRENCYPOSITION")
    private String currencyposition;

    /**
     * 請求金額
     */
    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    /**
     * 委託元役職(和文)
     */
    @Column(name = "PRPLACEPOSITIONJAPANESE")
    private String prplacepositionjapanese;

    /**
     * 委託元役職(中文)
     */
    @Column(name = "PRPLACEPOSITIONCHINESE")
    private String prplacepositionchinese;

    /**
     * 名前(和文)
     */
    @Column(name = "NAMEJAPANESE")
    private String namejapanese;

    /**
     * 名前(中文)
     */
    @Column(name = "NAMECHINESE")
    private String namechinese;

    /**
     * 基本契約書締結日
     */
    @Column(name = "SIGNINGDATE")
    private Date signingdate;

    /**
     * 技術内容（和文）
     */
    @Column(name = "TECHNICALCONTENTJAPANESE")
    private String technicalcontentjapanese;

    /**
     * 技術内容（中文）
     */
    @Column(name = "TECHNICALCONTENTCHINESE")
    private String technicalcontentchinese;

    /**
     * 再委託（和文）
     */
    @Column(name = "REDELEGATE")
    private String redelegate;

    /**
     * 再委託内容（和文）
     */
    @Column(name = "REDELEGATECONTENT")
    private String redelegatecontent;

    /**
     * 委託元社長(和文)
     */
    @Column(name = "COMPANYLEADERJAPANESE")
    private String companyleaderjapanese;

    /**
     * 委託元社長(中文)
     */
    @Column(name = "COMPANYLEADERCHINESE")
    private String companyleaderchinese;

    /**
     * その他特約事項（和文)
     */
    @Column(name = "OTHERTERMSJAPANESE")
    private String othertermsjapanese;

    /**
     * その他特約事項（中文）
     *
     */
    @Column(name = "OTHERTERMSCHINESE")
    private String othertermschinese;

    /**
     * 再委託（中文）
     */
    @Column(name = "SUBCONTRACT")
    private String subcontract;

    /**
     * 再委託内容（中文）
     */
    @Column(name = "SUBCONTRACTCONTENT")
    private String subcontractcontent;

}
