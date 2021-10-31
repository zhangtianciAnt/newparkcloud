package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "Judgementdetail")
public class Judgementdetail extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 多部门决裁
	 */
    @Id
    @Column(name = "JUDGEMENTDETAIL_ID")
    private String judgementdetail_id;

    /**
     * 决裁
     */
    @Column(name = "JUDGEMENT_ID")
    private String judgementid;

    /**
     * 多部门决裁-部门
     */
    @Column(name = "GROUP_NAMEM")
    private String group_nameM;

    /**
     * 多部门决裁-预算编码
     */
    @Column(name = "THISPROJECTM")
    private String thisprojectM;

    /**
     * 多部门决裁-事业计划
     */
    @Column(name = "CAREERPLANM")
    private String careerplanM;

    /**
     * 多部门决裁-事业计划类型
     */
    @Column(name = "BUSINESSPLANTYPEM")
    private String businessplantypeM;

    /**
     * 多部门决裁-分类类型
     */
    @Column(name = "CLASSIFICATIONTYPEM")
    private String classificationtypeM;

    /**
     * 多部门决裁-事业计划余额
     */
    @Column(name = "BUSINESSPLANBALANCEM")
    private String businessplanbalanceM;

    /**
     * 多部门决裁-实施计划金额
     */
    @Column(name = "AMOUNTTOBEGIVENM")
    private String amounttobegivenM;


    /**
     * 多部门决裁-实施计划金额
     */
    @Column(name = "RULINGID")
    private String rulingid;

    /**
     * 索引
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
