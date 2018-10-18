package com.nt.utils.services;

import com.nt.utils.TokenModel;

public interface TokenService {
    TokenModel setToken(TokenModel tokenModel);

    Boolean validToken(String token);

    TokenModel getToken(String token);
}
