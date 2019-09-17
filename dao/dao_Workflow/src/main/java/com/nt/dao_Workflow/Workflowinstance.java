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
@Table(name = "workflowinstance")
public class Workflowinstance extends BaseModel {

	private static final long serialVersionUID = 1L;
    /**
     * 流程实例ID
     */
    @Id
    @Column(name = "WORKFLOWINSTANCEID")
    private String workflowinstanceid;

    /**
     * 流程ID
     */
    @Column(name = "WORKFLOWID")
    private String workflowid;

    /**
     * 数据ID
     */
    @Column(name = "DATAID")
    private String dataid;

    /**
     * 流程名
     */
    @Column(name = "WORKFLOWNAME")
    private String workflowname;

    /**
     * 所属表单ID
     */
    @Column(name = "FORMID")
    private String formid;

    /**
     * 所属表单ID
     */
    @Column(name = "URL")
    private String url;

    /**
     * 流程属性
     */
    @Column(name = "WORKFLOWOPERATION")
    private String workflowoperation;

    /**
     * 流程图
     */
    @Column(name = "WORKFLOWCONTENT")
    private String workflowcontent;

    /**
     * 数据内容
     */
    @Column(name = "DATACONTENT")
    private String datacontent;

}
