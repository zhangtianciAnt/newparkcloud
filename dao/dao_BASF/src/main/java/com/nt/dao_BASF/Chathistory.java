package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @Classname Chathistory
 * @Description 聊天记录
 * @Date 2021/1/5 22:53
 * @Author skaixx
 */
@Data
public class Chathistory extends BaseModel {

    @Transient
    private String theadId;

    private String id;

    @Column(name = "`from`")
    private String from;

    @Column(name = "`to`")
    private String to;

    private String content;

    private String type;

    private Date time;

    @Transient
    private boolean isRead = true;
}
