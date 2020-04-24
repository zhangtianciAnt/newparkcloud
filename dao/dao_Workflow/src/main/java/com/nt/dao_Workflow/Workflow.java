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
@Table(name = "workflow")
public class Workflow extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 流程ID
     */
    @Id
    @Column(name = "WORKFLOWID")
    private String workflowid;

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
     * 流程图
     */
    @Column(name = "WORKFLOWCONTENT")
    private String workflowcontent;

    /**
     * 流程属性(1-自定义系统流程;2-用户自定义流程)
     */
    @Column(name = "WORKFLOWOPERATION")
    private String workflowoperation;

    /**
     * 流程说明
     */
    @Column(name = "WORKFLOWTEXT")
    private String workflowtext;

    /**
     * 识别码
     */
    @Column(name = "CODE")
    private String code;
}
