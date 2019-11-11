package com.nt.dao_Org;

import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Table(name = "todonotice")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoNotice extends BaseModel {
    /**
     * 数据主键ID
     */
    @Id
    @Column(name = "NOTICEID")
    private String noticeid;

    /**
     * 类型 1.审批流程代办；2.通知
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 标题
     */
    @Column(name = "TITLE")
    private String title;

    /**
     * 数据
     */
    @Column(name = "DATAID")
    private String dataid;

    /**
     * 页面
     */
    @Column(name = "URL")
    private String url;

    /**
     * 审批URL
     */
    @Column(name = "WORKFLOWURL")
    private String workflowurl;

    /**
     * 发起人
     */
    @Column(name = "INITIATOR")
    private String initiator;

    /**
     * 内容
     */
    @Column(name = "CONTENT")
    private String content;
}
