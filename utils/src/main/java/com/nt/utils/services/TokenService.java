package com.nt.utils.services;

import com.nt.utils.TokenModel;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {
    TokenModel setToken(TokenModel tokenModel);

    Boolean validToken(HttpServletRequest request);

    TokenModel getToken(HttpServletRequest request);
}
