package com.nt.dao_Org;

import com.nt.utils.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.InvocationTargetException;

@Document(collection = "user")
public class User extends BaseModel {
    private String id;
    private String userName;
    private String passWord;
    private String uppassWord;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUppassWord() {
        return uppassWord;
    }

    public void setUppassWord(String uppassWord) {
        this.uppassWord = uppassWord;
    }

}
