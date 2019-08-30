package com.nt.service_WorkFlow.mapper;

import java.util.List;

import com.nt.dao_Workflow.Vo.WorkflowLogVo;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface WorkflowinstanceMapper extends MyMapper<Workflowinstance> {

	List<WorkflowLogVo> ViewWorkflow(@Param("workflowinstance") Workflowinstance workflowinstance);

	// 流程结束
	void stopInstanceByReject(@Param("nodeid") String nodeid, @Param("status") String status);

}
