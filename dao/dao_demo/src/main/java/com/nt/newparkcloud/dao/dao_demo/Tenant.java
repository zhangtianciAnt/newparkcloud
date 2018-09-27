package com.nt.newparkcloud.dao.dao_demo;

import com.nt.newparkcloud.utils.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "tenant")
public class Tenant extends BaseModel {
    /**
     * 账号
     */
    private String accound;
    /**
     * 密码
     */
    private String password;

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
}
