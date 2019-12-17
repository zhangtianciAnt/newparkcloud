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
    private String personno;
    private String username;
    private String petname;
    private String title;
    private String sex;
    private String telephone;
    private String mobiletelephone;
    private String fax;
    private String email;
    private String webaddr;
    private String activity;
    private String userid;
    private String departmentid;
    private String allname;
}