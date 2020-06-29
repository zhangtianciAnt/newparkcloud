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
@Table(name = "napalm")
public class Napalm extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *納品書
     */
    @Id
    @Column(name = "NAPALM_ID")
    private String napalm_id;

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
     * 委託元（英文）
     */
    @Column(name = "DEPOSITENGLISH")
    private String depositenglish;

    /**
     * 委託元略
     */
    @Column(name = "ENTRUSTMENT")
    private String entrustment;

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

    /**
     * 請求番号
     */
    @Column(name = "CLAIMNUMBER")
    private String claimnumber;

    /**
     * 請求契約種類
     */
    @Column(name = "CLAIMTYPE")
    private String claimtype;

    /**
     * 納品作成日
     */
    @Column(name = "DELIVERYFINSHDATE")
    private Date deliveryfinshdate;

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
     * 通貨形式
     */
    @Column(name = "CURRENCYFORMAT")
    private String currencyformat;

    /**
     * 請求金額
     */
    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    /**
     * 到达TO
     */
    @Column(name = "TOTO")
    private String toto;

    /**
     * 出荷判定実施者
     */
    @Column(name = "LOADINGJUDGE")
    private String loadingjudge;

    /**
     * 該非判定書番号
     */
    @Column(name = "JUDGMENTNUMBER")
    private String judgmentnumber;

    /**
     * ウィルスチェック
     */
    @Column(name = "FULUSI")
    private String fulusi;

    /**
     * 担当会社名
     */
    @Column(name = "TAKECHARGE")
    private String takecharge;

    /**
     * 該非判定
     */
    @Column(name = "JUDGMENT")
    private String judgment;

    /**
     * 出荷判定
     */
    @Column(name = "DETERMINATION")
    private String determination;

    /**
     * 契約概要（/開発タイトル）和文
     */
    @Column(name = "CONJAPANESE")
    private String conjapanese;

    /**
     *
     */
    @Column(name = "CLAIMDATETIME")
    private String claimdatetime;

    /**
     * 请求日
     */
    @Column(name = "CLAIMDATE")
    private Date claimdate;
    /**
     * 印章状态
     */
    @Column(name = "SEALSTATUS")
    private String sealstatus;

    //印章ID
    @Column(name = "SEALID")
    private String sealid;
}
