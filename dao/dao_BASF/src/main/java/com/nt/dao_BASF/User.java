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
@Table(name = "user")
public class User extends BaseModel {
    @Id
    private String userid;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 电话号码
     */
    private String phonenumber;

    /**
     * 证件类别
     */
    private String documenttype;

    /**
     * 证件号
     */
    private String documentnumber;

    /**
     * 用户组id
     */
    private String usergroupid;

    /**
     * 性别
     */
    private String sex;

    /**
     * 岗位id
     */
    private String postid;

    /**
     * 门禁id
     */
    private String accessid;
}