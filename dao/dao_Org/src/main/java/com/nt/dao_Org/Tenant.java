package com.nt.dao_Org;

import com.nt.utils.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

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
