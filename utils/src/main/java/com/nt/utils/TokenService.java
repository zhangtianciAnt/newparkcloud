package com.nt.utils;

public interface TokenService {
    TokenModel  setToken(TokenModel tokenModel);

    Boolean validToken(String token);

}
