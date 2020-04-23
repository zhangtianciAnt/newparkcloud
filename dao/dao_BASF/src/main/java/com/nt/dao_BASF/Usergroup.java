package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usergroup")
public class Usergroup extends BaseModel {
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
}