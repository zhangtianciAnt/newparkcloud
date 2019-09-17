package com.nt.dao_Org;

import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String noticeid;

    /**
     * 类型 1.审批流程代办；2.通知
     */
    private String type;

    /**
     * 标题
     */
    private String title;

    /**
     * 数据
     */
    private String dataid;

    /**
     * 页面
     */
    private String url;

    /**
     * 审批URL
     */
    private String workflowurl;

    /**
     * 发起人
     */
    private String initiator;

    /**
     * 内容
     */
    private String content;
}
