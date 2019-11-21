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
@Table(name = "judgement")
public class Judgement extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 決裁願
	 */
    @Id
    @Column(name = "JUDGEMENT_ID")
    private String judgementid;

    /**
     * 申请人
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 申请センター
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 申请グループ
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 申请チーム
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * 起案者内線
     */
    @Column(name = "INVESTIGATOR")
    private String investigator;

    /**
     * 件名
     */
    @Column(name = "FILENAME")
    private String filename;

    /**
     * 実施予定日
     */
    @Column(name = "SCHEDULEDDATE")
    private String scheduleddate;

    /**
     * 事业计划
     */
    @Column(name = "CAREERPLAN")
    private String careerplan;

    /**
     * 事业计划类型
     */
    @Column(name = "BUSINESSPLANTYPE")
    private String businessplantype;

    /**
     * 分类类型
     */
    @Column(name = "CLASSIFICATIONTYPE")
    private String classificationtype;

    /**
     * 事业计划余额
     */
    @Column(name = "BUSINESSPLANBALANCE")
    private String businessplanbalance;

    /**
     * 実施予定金额
     */
    @Column(name = "AMOUNTTOBEGIVEN")
    private String amounttobegiven;

    /**
     * 要旨
     */
    @Column(name = "GIST")
    private String gist;

    /**
     * 購入先名/支払い先名
     */
    @Column(name = "PURCHASSUPPORT")
    private String purchassupport;

    /**
     * 数量
     */
    @Column(name = "NUMBERS")
    private String numbers;

    /**
     * 单价
     */
    @Column(name = "UNITPRICE")
    private String unitprice;

    /**
     * 金额
     */
    @Column(name = "MONEY")
    private String money;

    /**
     * プロジェクト（予算コード）
     */
    @Column(name = "THISPROJECT")
    private String thisproject;

    /**
     * 設置場所（ 部屋No. ）
     */
        @Column(name = "SETTINGPLACE")
    private String settingplace;

    /**
     * 添付書類
     */
    @Column(name = "ADDBOOK")
    private String addbook;

    /**
     * 出售报价单
     */
    @Column(name = "SALEQUOTATION")
    private String salequotation;

    /**
     * 不足两份报价单理由
     */
    @Column(name = "REASONSFORQUOTATION")
    private String reasonsforquotation;

    /**
     * 附件
     */
    @Column(name = "UPLOADFILE")
    private Date uploadfile;

    /**
     * 無償设备
     */
    @Column(name = "FREEDEVICE")
    private String freedevice;

    /**
     * 設備
     */
    @Column(name = "EQUIPMENT")
    private String equipment;

    /**
     * 决裁类型
     */
    @Column(name = "DECISIVE")
    private String decisive;

    /**EQUIPMENT
     * 折旧期限
     */
    @Column(name = "PERIOD")
    private String period;

    /**
     * 开始时间
     */
    @Column(name = "STARTDATE")
    private Date startdate;

    /**
     * 结束时间
     */
    @Column(name = "ENDDATE")
    private Date enddate;


}
