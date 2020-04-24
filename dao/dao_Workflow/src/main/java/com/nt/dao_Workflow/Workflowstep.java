package com.nt.dao_Workflow;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "workflowstep")
public class Workflowstep extends BaseModel {

	private static final long serialVersionUID = 1L;
	/**
	 * 审批节点操作ID
	 */
	@Id
	@Column(name = "WORKFLOWSTEPID")
	private String workflowstepid;

	/**
	 * 流程节点实例ID
	 */
	@Column(name = "WORKFLOWNODEINSTANCEID")
	private String workflownodeinstanceid;

	/**
	 * 审批结果
	 */
	@Column(name = "RESULT")
	private String result;

	/**
	 * 步骤名
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 用户ID
	 */
	@Column(name = "ITEMID")
	private String itemid;

	/**
	 * 备注
	 */
	@Column(name = "REMARK")
	private String remark;

}
