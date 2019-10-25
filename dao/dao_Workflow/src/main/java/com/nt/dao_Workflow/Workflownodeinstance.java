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
@Table(name = "workflownodeinstance")
public class Workflownodeinstance extends BaseModel {

	private static final long serialVersionUID = 1L;
    /**
     * 流程节点实例ID
     */
    @Id
    @Column(name = "WORKFLOWNODEINSTANCEID")
    private String workflownodeinstanceid;

    /**
     * 流程实例ID
     */
    @Column(name = "WORKFLOWINSTANCEID")
    private String workflowinstanceid;

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
     * 节点类型(1-审批;2-确认;3-循环)
     */
    @Column(name = "NODETYPE")
    private String nodetype;

    /**
     * 节点人员类型(1-指定人，2-上级领导，3-特殊指定)
     */
    @Column(name = "NODEUSERTYPE")
    private String nodeusertype;

    /**
     * 审批人
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

    /**
     * 注意事项
     */
    @Column(name = "REMARKS")
    private String remarks;
}
