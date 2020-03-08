package com.nt.utils.dao;


import com.nt.utils.StringUtils;

/**
 * 微信凭证对象
 *
 * @author wangsong
 */
public class AccessToken extends WxBaseResponse {
    private String access_token; // 获取到的凭证
    private long   expires_in;   // 凭证有效时间，单位：秒，一般是7200

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        // 考虑到服务器时间同步,故将刷新时间提前60秒.
        this.expires_in = System.currentTimeMillis() + (expires_in - 60) * 1000;
    }

    /**
     * 判断access_token是否过期
     *
     * @return
     */
    public boolean isAvailable() {
        if (!StringUtils.isEmpty(access_token) && this.expires_in >= System.currentTimeMillis()) { return true; }
        return false;
    }

}
