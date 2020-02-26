package com.nt.utils.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存AccessToken信息
 *
 * @author shenjian
 */
public class AccessTokenCache {

    // 以appid作为key值缓存每个公众号的access_token
    public static Map<String, AccessToken> tokenMap = new ConcurrentHashMap<String, AccessToken>();
}
