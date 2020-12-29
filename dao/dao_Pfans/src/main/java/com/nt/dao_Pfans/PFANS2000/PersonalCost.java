package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "personalcost")
public class PersonalCost extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 人件费ID
	 */
    @Id
    @Column(name = "PERSONALCOSTID")
    private String personalcostid;

    /**
     * 人件费年度ID
     */
    @Column(name = "YEARSANTID")
    private String yearsantid;

    /**
     *CENTERID
     */
    @Column(name = "CENTERID ")
    private String centerid ;

    /**
     *GROUPID
     */
    @Column(name = "GROUPID ")
    private String groupid ;

    /**
     * 姓名id
     */
    @Column(name = "USERID")
    private String userid;

    /**
     * 姓名name
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * 部门简称
     */
    @Column(name = "DEPARTSHORT")
    private String departshort;

    /**
     * 配付与否
     */
    @Column(name = "ALLOTMENT")
    private String allotment;

    /**
     * 新人入社预定月
     */
    @Column(name = "NEWPERSONALDATE")
    private Date newpersonaldate;

    /**
     * 升格前Rn
     */
    @Column(name = "EXRANK")
    private String exrank;

    /**
     * 是否升格升号
     */
    @Column(name = "CHANGERANK")
    private String changerank;

    /**
     * 升格后Rn
     */
    @Column(name = "LTRANK")
    private String ltrank;

    /**
     * 基本给
     */
    @Column(name = "BASICALLYANT")
    private String basicallyant;

    /**
     * 职责给
     */
    @Column(name = "RESPONSIBILITYANT")
    private String responsibilityant;

    /**
     * 月工资
     */
    @Column(name = "MONTHLYSALARY")
    private String monthlysalary;

    /**
     * 一括补贴
     */
    @Column(name = "ALLOWANCEANT")
    private String allowanceant;

    /**
     * 拓展项补贴1（备份)
     */
    @Column(name = "OTHERANTONE")
    private String otherantone;

    /**
     * 拓展项补贴2（备份)
     */
    @Column(name = "OTHERANTTWO")
    private String otheranttwo;

    /**
     * 独生子女费
     */
    @Column(name = "ONLYCHILD")
    private String onlychild;

    /**
     * 补贴总计
     */
    @Column(name = "TOTALSUBSIDIES")
    private String totalsubsidies;

    /**
     * 月度奖金月数
     */
    @Column(name = "MONTHLYBONUSMONTHS")
    private String monthlybonusmonths;

    /**
     * 月度奖金
     */
    @Column(name = "MONTHLYBONUS")
    private String monthlybonus;

    /**
     * 年度奖金月数
     */
    @Column(name = "ANNUALBONUSMONTHS")
    private String annualbonusmonths;

    /**
     * 年度奖金
     */
    @Column(name = "ANNUALBONUS")
    private String annualbonus;

    /**
     * 工资总额
     */
    @Column(name = "TOTALWAGES")
    private String totalwages;

    /**
     * 工会经费
     */
    @Column(name = "TRADEUNIONFUNDS")
    private String tradeunionfunds;

    /**
     * 加班费时给
     */
    @Column(name = "OVERTIMEPAY")
    private String overtimepay;

    /**
     * 是否大连户籍
     */
    @Column(name = "INDALIAN")
    private String indalian;

    /**
     * 养老保险基4
     */
    @Column(name = "OLDYLBXJAJ")
    private String oldylbxjaj;

    /**
     * 失业保险基4
     */
    @Column(name = "LOSSYBXJAJ")
    private String lossybxjaj;

    /**
     * 工伤保险基4
     */
    @Column(name = "GSBXJAJ")
    private String gsbxjaj;

    /**
     * 医疗保险基4
     */
    @Column(name = "YLBXJAJ")
    private String ylbxjaj;

    /**
     * 生育保险基4
     */
    @Column(name = "SYBXJAJ")
    private String sybxjaj;

    /**
     * 公积金基数4
     */
    @Column(name = "GJJJSAJ")
    private String gjjjsaj;

    /**
     * 社保企业4
     */
    @Column(name = "SBQYAJ")
    private String sbqyaj;

    /**
     * 取暖补贴4
     */
    @Column(name = "QNBUAJ")
    private String qnbuaj;

    /**
     * 大病险4
     */
    @Column(name = "DBXAJ")
    private String dbxaj;

    /**
     * 社保公司4
     */
    @Column(name = "SBGSAJ")
    private String sbgsaj;

    /**
     * 公积金公司负担4
     */
    @Column(name = "GJJGSFDAJ")
    private String gjjgsfdaj;

    /**
     * 4月-6月
     */
    @Column(name = "APTOJU")
    private String aptoju;

    /**
     * 养老保险基7
     */
    @Column(name = "OLDYLBXJJM")
    private String oldylbxjjm;

    /**
     * 失业保险基7
     */
    @Column(name = "LOSSYBXJJM")
    private String lossybxjjm;

    /**
     * 工伤保险基7
     */
    @Column(name = "GSBXJJM")
    private String gsbxjjm;

    /**
     * 医疗保险基7
     */
    @Column(name = "YLBXJJM")
    private String ylbxjjm;

    /**
     * 生育保险基7
     */
    @Column(name = "SYBXJJM")
    private String sybxjjm;

    /**
     * 公积金基数7
     */
    @Column(name = "GJJJSJM")
    private String gjjjsjm;

    /**
     * 社保企业7
     */
    @Column(name = "SBQYJM")
    private String sbqyjm;

    /**
     * 取暖补贴7
     */
    @Column(name = "QNBUJM")
    private String qnbujm;

    /**
     * 大病险7
     */
    @Column(name = "DBXJM")
    private String dbxjm;

    /**
     * 社保公司7
     */
    @Column(name = "SBGSJM")
    private String sbgsjm;

    /**
     * 公积金公司负担7
     */
    @Column(name = "GJJGSFDJM")
    private String gjjgsfdjm;

    /**
     * 7月-3月
     */
    @Column(name = "JUTOMA")
    private String jutoma;

    /**
     * 创建人
     */
    @Column(name = "CREATEBY")
    private String createby;

    /**
     * 创建时间
     */
    @Column(name = "CREATEON")
    private Date createon;

    /**
     * 更新人
     */
    @Column(name = "MODIFYBY")
    private String modifyby;

    /**
     * 更新时间
     */
    @Column(name = "MODIFYON")
    private Date modifyon;

    /**
     * 负责人
     */
    @Column(name = "OWNER")
    private String owner;

    /**
     * 状态
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 租户id
     */
    @Column(name = "TENANTID")
    private String tenantid;
}