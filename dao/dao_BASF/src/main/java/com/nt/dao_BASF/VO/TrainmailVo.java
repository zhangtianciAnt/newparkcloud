package com.nt.dao_BASF.VO;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainmailVo extends BaseModel {
    /**
     * 主键
     */
    @Id
    private String trainmailid;

    /**
     * 培训邮件设置名称
     */
    private String trainmailname;

    /**
     * 邮件地址
     */
    private String addressee;

    /**
     * 备注
     */
    private String remark;
}
