package com.nt.service_WorkFlow.mapper;

import java.util.List;

import com.nt.dao_Workflow.Vo.WorkflowLogVo;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface WorkflowinstanceMapper extends MyMapper<Workflowinstance> {

	List<WorkflowLogVo> ViewWorkflow(@Param("workflowinstance") Workflowinstance workflowinstance);

	// 流程结束
	void stopInstanceByReject(@Param("nodeid") String nodeid, @Param("status") String status);

	@Select("SELECT ITEMID FROM workflowstep where WORKFLOWNODEINSTANCEID = (select WORKFLOWNODEINSTANCEID from workflownodeinstance where WORKFLOWINSTANCEID = #{workflowinstanceid} and NODEORD = '1')")
	String getItemid(@Param("workflowinstanceid") String workflowinstanceid);

}
