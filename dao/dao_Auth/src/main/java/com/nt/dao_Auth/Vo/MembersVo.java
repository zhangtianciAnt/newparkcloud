package com.nt.dao_Auth.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Auth
 * @ClassName: MembersVo
 * @Description: 角色成员Vo
 * @Author: WENCHAO
 * @CreateDate: 2018/12/10
 * @UpdateUser: WENCHAO
 * @UpdateDate: 2018/12/10
 * @UpdateRemark: 新建
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembersVo {
    /**
     * 用户主键ID
     */
    private String _id;
    /**
     * USERID
     */
    private String userid;
    /**
     * 姓名
     */
    private String customername;
    /**
     * 手机
     */
    private String mobilenumber;
    /**
     * 部门
     */
    private List<String> departments;
}
