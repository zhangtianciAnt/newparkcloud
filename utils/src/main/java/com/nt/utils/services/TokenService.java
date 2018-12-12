package com.nt.utils.services;



import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;

/**
 * 对token进行操作的管理接口
 *
 * @author SKAIXX
 */
public interface TokenService {

    TokenModel setToken(TokenModel tokenModel);

    Boolean validToken(HttpServletRequest request);

    TokenModel getToken(HttpServletRequest request);

    TokenModel getToken(String token);

    void clearToken(HttpServletRequest request);

}
