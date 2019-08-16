package com.nt.dao_Workflow.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationWorkflowVo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String menuUrl;

	private String dataId;

	private String result;

	private String toAnotherUser;

	private String users;

	private String remark;

	private String userid;

	private String tenantid;

}
