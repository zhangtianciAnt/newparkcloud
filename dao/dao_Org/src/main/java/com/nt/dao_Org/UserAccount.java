package com.nt.dao_Org;
import com.nt.utils.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "useraccount")
public class UserAccount extends BaseModel {
    /**
     * 用户id
     */
    private String userid;
    /**
     * 账号
     */
    private String accound;
    /**
     * 密码
     */
    private String password;
    /**
     * openid
     */
    private String openid;
    /**
     * 账户类型
     */
    private String usertype;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAccound() {
        return accound;
    }

    public void setAccound(String accound) {
        this.accound = accound;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
