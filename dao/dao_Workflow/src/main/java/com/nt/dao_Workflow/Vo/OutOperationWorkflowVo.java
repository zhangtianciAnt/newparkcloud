package com.nt.dao_Workflow.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutOperationWorkflowVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String state;

    private String workflowCode;
}
