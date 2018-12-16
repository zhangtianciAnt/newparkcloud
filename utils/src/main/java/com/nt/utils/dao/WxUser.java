package com.nt.utils.dao;
import java.io.Serializable;
import com.alibaba.fastjson.JSON;

/**
 * 微信用户信息
 *
 * @author zhangying
 *
 */
public class WxUser implements Serializable {

    private static final long serialVersionUID = 1L;
    protected Boolean         subscribe;
    protected String          openId;
    protected String          nickname;
    protected String          sex;
    protected String          language;
    protected String          city;
    protected String          province;
    protected String          country;
    protected String          headImgUrl;
    protected Long            subscribeTime;
    protected String          unionId;
    protected Integer         sexId;
    protected String          remark;
    protected Integer         groupId;

    public static WxUser fromJson(String json) {
        return JSON.parseObject(json, WxUser.class);
    }

    @Override
    public String toString() {
        return "WxUser{" + "subscribe=" + subscribe + ", openId='" + openId + '\'' + ", nickname='"
                + nickname + '\'' + ", sex='" + sex + '\'' + ", language='" + language + '\''
                + ", city='" + city + '\'' + ", province='" + province + '\'' + ", country='"
                + country + '\'' + ", headImgUrl='" + headImgUrl + '\'' + ", subscribeTime="
                + subscribeTime + ", unionId='" + unionId + '\'' + ", remark='" + remark + '\''
                + ", groupId='" + groupId + '\'' + '}';
    }

}
