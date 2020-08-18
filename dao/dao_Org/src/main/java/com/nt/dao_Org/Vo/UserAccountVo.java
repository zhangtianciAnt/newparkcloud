package com.nt.dao_Org.Vo;

import cn.hutool.core.codec.Base64;
import com.nt.utils.StringUtils;
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
 * @Version: 1.0
 */
@Document(collection = "userAccountVo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountVo extends BaseModel {
    /**
     * 原密码 CHANGEPASSWORD
     */
    private String password;

    public void setPassword(String password) {
        if(!StringUtils.isBase64Encode(password)){
            this.password = Base64.encode(password);
        }else{
            this.password = password;
        }
    }

    public void setNewpsw(String newpsw) {
        this.newpsw = newpsw;
    }

    /**
     * 新密码 NEWPSW
     */
    private String newpsw;
    /**
     * 用户ID USERID
     */
    private String userid;
}

