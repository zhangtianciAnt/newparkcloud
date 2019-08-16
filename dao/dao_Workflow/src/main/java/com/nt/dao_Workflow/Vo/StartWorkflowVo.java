package com.nt.dao_Workflow.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartWorkflowVo implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String menuUrl;

	private String dataId;

	private String workFlowId;

	private String tenantId;

	private String userId;

	private List<String> userList;
}
