package com.nt.newparkcloud.utils;

public interface TokenService {
    TokenModel  setToken(TokenModel tokenModel);

    Boolean validToken(String token);

}
