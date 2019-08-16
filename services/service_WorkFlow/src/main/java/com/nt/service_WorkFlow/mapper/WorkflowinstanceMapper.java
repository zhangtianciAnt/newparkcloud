package com.nt.service_WorkFlow.mapper;

import java.util.List;

import com.nt.dao_Workflow.Vo.WorkflowLogVo;
import com.nt.dao_Workflow.Workflowinstance;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface WorkflowinstanceMapper extends Mapper<Workflowinstance> {

	List<WorkflowLogVo> ViewWorkflow(@Param("workflowinstance") Workflowinstance workflowinstance);

	// 流程结束
	void stopInstanceByReject(@Param("nodeid") String nodeid, @Param("status") String status);

}
