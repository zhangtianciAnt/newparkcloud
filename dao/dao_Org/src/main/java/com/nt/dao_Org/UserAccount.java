package com.nt.dao_Org;


import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: UserAccound
 * @Description: 用户账户
 * @Author: SKAIXX
 * @CreateDate: 2018/10/25
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/10/25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Document(collection = "useraccount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount extends BaseModel {

    /**
     * 数据主键ID
     */
    private String _id;
    /**
     * 用户ID
     */
    private String userid;

    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * openid
     */
    private String openid;
    /**
     * 账户类型 0:内部;1:外部
     */
    private String usertype;
    /**
     * 系统角色
     */
    private List<String> roles;

}
