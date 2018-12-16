package com.nt.utils.dao;
import java.io.Serializable;
import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class WxEnterpriseUser implements Serializable {
    protected Integer         errcode;
    protected String          errmsg;
    protected String          UserId;
    protected String          DeviceId;
    protected String          user_ticket;
    protected String          expires_in;

    public static WxEnterpriseUser fromJson(String json) {
        return JSON.parseObject(json, WxEnterpriseUser.class);
    }

    @Override
    public String toString() {
        return "WxEnterpriseUser{" + "errcode=" + errcode + ", errmsg='" + errmsg + '\'' + ", UserId='"
                + UserId + '\'' + ", DeviceId='" + DeviceId + '\'' + ", user_ticket='" + user_ticket + '\''
                + ", expires_in='" + expires_in + '\'' + '}';
    }
}
