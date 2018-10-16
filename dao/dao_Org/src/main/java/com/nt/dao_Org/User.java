package com.nt.dao_Org;

import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", uppassWord='" + uppassWord + '\'' +
                '}';
    }
}
