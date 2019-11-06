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
public class UsergroupVo extends BaseModel {
    @Id
    private String usergroupid;

    /**
     * 用户组名称
     */
    private String usergroupname;

    /**
     * 用户组说明
     */
    private String remark;

    /**
     * 用户组成员
     */
    private String teammember;
}