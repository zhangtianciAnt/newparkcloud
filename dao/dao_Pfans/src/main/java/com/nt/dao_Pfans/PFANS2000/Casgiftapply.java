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
@Table(name = "casgiftapply")
public class Casgiftapply extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 祝礼金申请ID
	 */
    @Id

    private String casgiftapply_id;

    /**
     * 一级分类ID
     */
    @Column(name = "FIRSTCLASS")
    private String firstclass;

    /**
     * 二级分类ID
     */
    @Column(name = "TWOCLASS")
    private String twoclass;

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
     * 申请人ID
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 所属部门ID
     */
    @Column(name = "DEPARTMENT_ID")
    private String department_id;

    /**
     * 申请日期
     */

    private Date application_date;

    /**
     * 申请金额（元）
     */
    @Column(name = "AMOUNTMONEY")
    private String amoutmoney;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 附件
     */
        @Column(name = "UPLOADFILE")
    private String uploadfile;

    /**
     * 创建人
     */
    @Column(name = "CREATEBY")
    private String CREATEBY;

    /**
     * 创建时间
     */
    @Column(name = "CREATEON")
    private Date CREATEON;

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

}
