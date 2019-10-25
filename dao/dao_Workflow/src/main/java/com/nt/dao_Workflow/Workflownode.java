package com.nt.dao_Workflow;

import java.util.Date;
import javax.persistence.*;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "workflownode")
public class Workflownode extends BaseModel {

	private static final long serialVersionUID = 1L;
    /**
     * 流程节点ID
     */
    @Id
    @Column(name = "WORKFLOWNODEID")
    private String workflownodeid;

    /**
     * 流程ID
     */
    @Column(name = "WORKFLOWID")
    private String workflowid;

    /**
     * 节点名
     */
    @Column(name = "NODENAME")
    private String nodename;

    /**
     * 节点顺序
     */
    @Column(name = "NODEORD")
    private Integer nodeord;

    /**
     * 节点类型(1-确认;2-审批;3-循环)
     */
    @Column(name = "NODETYPE")
    private String nodetype;

    /**
     * 节点人员类型(1-指定人，2-上级领导，3-特殊指定)
     */
    @Column(name = "NODEUSERTYPE")
    private String nodeusertype;

    /**
     * 审批人(1-用户ID;)
     */
    @Column(name = "ITEMID")
    private String itemid;

    /**
     * 抄送
     */
    @Column(name = "CC")
    private String cc;

    /**
     * 出口条件
     */
    @Column(name = "OUTCONDITION")
    private String outcondition;

}
