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
@Table(name = "business")
public class Business extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 出张申请ID
     */
    @Id
    @Column(name = "BUSINESS_ID")
    private String businessid;

    /**
     * 申请人ID
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 所属グループID
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 所属チームID
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 所属センターID
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * 申请日期
     */
    @Column(name = "APPLICATION_DATE")
    private Date applicationdate;

    /**
     * 出差分类
     */
    @Column(name = "BUSINESSTYPE")
    private String businesstype;

    /**
     * 职员等级
     */
    @Column(name = "LEVEL")
    private String level;

    /**
     * 项目名称
     */
    @Column(name = "PROJECTNAME")
    private String projectname;

    /**
     * 出張先会社（出差公司）
     */
    @Column(name = "CONDOMINIUMCOMPANY")
    private String condominiumcompany;

    /**
     * 出張地域
     */
    @Column(name = "REGION")
    private String region;

    /**
     * 出張先国家・都市（出差地）
     */
    @Column(name = "CITY")
    private String city;

    /**
     * 出発日（出发日）
     */
    @Column(name = "STARTDATE")
    private Date startdate;

    /**
     * 帰国日（回程日）
     */
    @Column(name = "ENDDATE")
    private Date enddate;

    /**
     * 合計(日間)（合计日数）
     */
    @Column(name = "DATENUMBER")
    private String datenumber;

    /**
     * 境外初回出張
     */
    @Column(name = "ABROADBUSINESS")
    private String abroadbusiness;

    /**
     * 出差目的类型
     */
    @Column(name = "OBJECTIVETYPE")
    private String objectivetype;

    /**
     * 出差目的类型其他
     */
    @Column(name = "OBJECTIVETYPEOTHER")
    private String objectivetypeother;

    /**
     * 出差目的详情
     */
    @Column(name = "DETAILS")
    private String details;

    /**
     * 预算单位
     */
    @Column(name = "BUDGETUNIT")
    private String budgetunit;

    /**
     * Passport No.
     */
    @Column(name = "PASSPORTNO")
    private String passportno;

    /**
     * Passport有効期間开始
     */
    @Column(name = "DURATIONSTART")
    private Date durationstart;

    /**
     * Passport有効期間结束
     */
    @Column(name = "DURATIONEND")
    private Date durationend;

    /**
     * 事業計画（事业计划）
     */
    @Column(name = "PLAN")
    private String plan;

    /**
     * 事业计划类型
     */
    @Column(name = "PLANTYPE")
    private String plantype;

    /**
     * 分类类型
     */
    @Column(name = "CLASSIFICATIONTYPE")
    private String classificationtype;

    /**
     * 事业计划余额
     */
    @Column(name = "BALANCE")
    private String balance;

    /**
     * 金额
     */
    @Column(name = "MONEYS")
    private String moneys;

    /**
     * 币种
     */
    @Column(name = "CURRENCY")
    private String currency;

    /**
     * 日元汇率
     */
    @Column(name = "JPYFXRATE")
    private String jpyfxrate;

    /**
     * 美元汇率
     */
    @Column(name = "DOLLARFXRATE")
    private String dollarfxrate;

    /**
     * 其他汇率
     */
    @Column(name = "OTHERFXRATE")
    private String otherfxrate;

    /**
     * 外币金额
     */
    @Column(name = "FOREIGNCURRENCY")
    private String foreigncurrency;

    /**
     * 机票预订日
     */
    @Column(name = "BOOKINGDAY")
    private Date bookingday;

    /**
     * 精算予定日
     */
    @Column(name = "ACTUARIALDATE")
    private Date actuarialdate;

    /**
     * 招聘状
     */
    @Column(name = "RECRUITMENT")
    private String recruitment;

    /**
     * 仮出金日（借款日）
     */
    @Column(name = "LOANDAY")
    private Date loanday;

    /**
     * 仮出金額（元)（借款金额）
     */
    @Column(name = "loanmoney")
    private String Loanmoney;

    /**
     * 宿泊費用負担（住宿费负担）
     */
    @Column(name = "ACCOMMODATIONCOST")
    private String accommodationcost;

    /**
     * 宿泊費用精算
     */
    @Column(name = "ACCOMMODATIONEXPENSES")
    private String accommodationexpenses;

    /**
     * 宿泊施設（住宿设施）
     */
    @Column(name = "ACCOMMODATION")
    private String accommodation;

    /**
     * Visa取得
     */
    @Column(name = "VISA")
    private String visa;

    /**
     * Visa入国有効期間开始
     */
    @Column(name = "VALIDSTART")
    private Date validstart;

    /**
     * Visa入国有効期間结束
     */
    @Column(name = "VALIDEND")
    private Date validend;

    /**
     * Visa滞在許可期間
     */
    @Column(name = "PERMIT")
    private String permit;

    /**
     * 滞在予定期間
     */
    @Column(name = "SCHEDULED")
    private String scheduled;

    /**
     * 技術の提供
     */
    @Column(name = "PROVISION")
    private String provision;

    /**
     * 技術内容
     */
    @Column(name = "TECHNOLOGY")
    private String technology;

    /**
     * 該非判定
     */
    @Column(name = "JUDGMENT")
    private String judgment;

    /**
     * 該当判定No
     */
    @Column(name = "JUDGMENTNO")
    private String judgmentno;

    /**
     * ﾊﾝﾄﾞｷｬﾘｰ（PC持出）
     */
    @Column(name = "PASSENGERS")
    private String passengers;

    /**
     * 固定資産番号（固定资产编号）
     */
    @Column(name = "FIXEDASSETSNO")
    private String fixedassetsno;

    /**
     * 规定外费用
     */
    @Column(name = "EXTERNAL")
    private String external;

    /**
     * 規定外費用発生説明（规定外费用发生原因）
     */
    @Column(name = "REASON")
    private String reason;

    /**
     * 规定外费用金额
     */
    @Column(name = "REGULATIONS")
    private String regulations;

    /**
     * 他の説明（其他说明）
     */
    @Column(name = "OTHEREXPLANATION")
    private String otherexplanation;


}
