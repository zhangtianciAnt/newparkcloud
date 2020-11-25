package com.nt.dao_Pfans.PFANS2000;

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
     * 姓名
     */
    @Column(name = "USERID")
    private String userid;

    /**
     * 等级
     */
    @Column(name = "PRANK")
    private String prank;

    /**
     * 在职状态
     */
    @Column(name = "ONJOB")
    private String onjob;

    /**
     * 4~6月人件费
     */
    @Column(name = "APTOJUCOST")
    private String aptojucost;

    /**
     * 7~3月人件费 1月1日自动生成
     */
    @Column(name = "JUTOMACOST")
    private String jutomacost;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

}
