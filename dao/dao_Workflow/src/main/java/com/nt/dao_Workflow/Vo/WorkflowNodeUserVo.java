package com.nt.dao_Workflow.Vo;

import com.nt.dao_Workflow.Workflownode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowNodeUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;

    private String uid;

    private String department;

    private String index;
}
