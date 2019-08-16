package com.nt.dao_Workflow.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowLogDetailVo implements Serializable {
	/**
	*
	*/
	private static final long serialVersionUID = 1L;
	// 操作人
	private String userId;
	// 操作结果
	private String result;
	// 操作备注
	private String remark;
	// 操作开始时间
	private Date sdata;
	// 操作结束时间
	private Date edata;
}
