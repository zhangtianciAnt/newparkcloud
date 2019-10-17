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
public class WorkflowVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String workflowid;

    private String workflowname;

    private String code;

    private String formid;

    private String workflowtext;

    private List<Workflownode> nodeList;
}
