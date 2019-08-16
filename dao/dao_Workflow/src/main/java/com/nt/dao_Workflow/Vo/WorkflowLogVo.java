package com.nt.dao_Workflow.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowLogVo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// 节点ID
	private String id;
	// 节点名
	private String title;
	// 节点状态
	private String stepStatus;

	private List<WorkflowLogDetailVo> detail;

}

