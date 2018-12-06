package com.nt.dao_Org.Vo;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: UserAccountVo
 * @Description: 修改密码
 * @Author: wangshuai
 * @CreateDate: 2018/11/05
 * @UpdateUser: wangshuai
 * @UpdateDate: 2018/11/05
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Document(collection = "userAccountVo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountVo  extends BaseModel {
    /**
     * 原密码 CHANGEPASSWORD
     */
    private String password;
    /**
     * 新密码 NEWPSW
     */
    private String newpsw;
    /**
     * 用户ID USERID
     */
    private String userid;
}

