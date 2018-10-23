package com.nt.utils.services;

import cn.hutool.crypto.SecureUtil;
import com.nt.utils.AuthConstants;
import com.nt.utils.TokenModel;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 对token进行操作的管理接口
 *
 * @author SKAIXX
 */
public interface TokenService {

    TokenModel setToken(TokenModel tokenModel);

    Boolean validToken(HttpServletRequest request);

    TokenModel getToken(HttpServletRequest request);

}