package com.nt.dao_BASF;


import java.io.Serializable;


public class SendEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFromAddress() {
        return fromaddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromaddress = fromAddress;
    }

    public String getToAddress() {
        return toaddress;
    }

    public void setToAddress(String toAddress) {
        this.toaddress = toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContextType() {
        return contexttype;
    }

    public void setContextType(String contextType) {
        this.contexttype = contextType;
    }

    //邮箱服务器地址
    private String host;
    //主机端口
    private Integer port;
    //发送者的邮箱账号
    private String username;
    //发送者的密码
    private String password;
    //发送者的邮箱地址
    private String fromaddress;
    //接收者的邮箱地址
    private String toaddress;
    //设置邮件主题
    private String subject;
    //设置邮件内容
    private String context;
    //设置邮件类型
    private String contexttype;
}