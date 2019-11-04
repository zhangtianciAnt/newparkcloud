package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "emailmessage")
public class Emailmessage extends BaseModel {
    /**
     * 邮件短息主键
     */
    @Id
    private String emailmesageid;

    /**
     * 模板名称
     */
    private String templatename;

    /**
     * 邮件/短信分类
     */
    private String templatetype;

    /**
     * 一级分类
     */
    private String firsttype;

    /**
     * 二级分类
     */
    private String secondtype;

    /**
     * 接收人
     */
    private String addressee;

    /**
     * 抄送人
     */
    private String copyperson;

    /**
     * 主题
     */
    private String theme;

    /**
     * 内容
     */
    private String content;
}