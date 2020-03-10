package com.nt.dao_Pfans.PFANS2000;

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
@Table(name = "bonussend")
public class Bonussend extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 奖金发送
	 */
    @Id
    @Column(name = "BONUSSEND_ID")
    private String bonussend_id;

    /**
     * 工号
     */
    @Column(name = "JOBNUMBER")
    private String jobnumber;

    /**
     * 名前ID
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 姓名
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * 奖金总额（元）
     */
    @Column(name = "TOTALBONUS1")
    private String totalbonus1;

    /**
     * 纳税方法（个人选择）
     */
    @Column(name = "METHOD")
    private String method;

    /**
     * 综合收入应纳税金（元）
     */
    @Column(name = "TAXABLE")
    private String taxable;

    /**
     * 工资已纳税金额（元）
     */
    @Column(name = "AMOUNT")
    private String amount;

    /**
     * 应补缴税额（元）
     */
    @Column(name = "PAYABLE")
    private String payable;

    /**
     * 一次性奖金应纳税所得额月平均额（元）
     */
    @Column(name = "INCOME")
    private String income;

    /**
     * 一次性奖金税率（%）
     */
    @Column(name = "TAXRATE")
    private String taxrate;

    /**
     * 一次性奖金速算扣除数（元）
     */
    @Column(name = "DEDUCTIONS")
    private String deductions;

    /**
     * 一次性奖金税金（元）
     */
    @Column(name = "BONUSTAX")
    private String bonustax;

    /**
     * 到手金额（元）
     */
    @Column(name = "RECEIVED")
    private String received;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 送信済
     */
    @Column(name = "SENT")
    private String sent;

}
