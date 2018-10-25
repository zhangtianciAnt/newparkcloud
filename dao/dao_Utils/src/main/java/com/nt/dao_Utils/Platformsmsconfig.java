package com.nt.dao_Utils;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Platformsmsconfig extends BaseModel implements Serializable{
    /**
     * ID
     */
    private String _id;

    /**
     * 标题
     */
    private String title;

    /**
     * APP_KEY
     */
    private String appkey;

    /**
     * APP_SECRET
     */
    private String appsecret;

    /**
     * HOST
     */
    private String host;

    /**
     * 签名
     */
    private String sign;
}