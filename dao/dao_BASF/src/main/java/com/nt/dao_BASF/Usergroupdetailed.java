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
@Table(name = "usergroupdetailed")
public class Usergroupdetailed extends BaseModel {
    @Id
    private String Usergroupdetailedid;

    /**
     * 所属用户组
     */
    private String usergroupid;

    /**
     * 用户组组员
     */
    private String teammember;
}