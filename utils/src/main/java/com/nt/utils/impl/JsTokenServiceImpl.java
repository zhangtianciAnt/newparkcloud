package com.nt.utils.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nt.utils.AuthConstants;
import com.nt.utils.JedisUtils;
import com.nt.utils.MD5Utils;
import com.nt.utils.dao.JsTokenModel;
import com.nt.utils.services.JsTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;



/**
 * 通过Redis存储和验证token的实现类
 *
 * @author shenjian
 */
@Component
public class JsTokenServiceImpl implements JsTokenService {

	Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);
	// token存储前缀
	private final static String TOKEN_PREFIX = "token_";

	// 用户存储前缀
	private final static String USER_PREFIX = "user_";

	/**
     * 创建一个token关联上指定用户
     * @param userId 指定用户的id
     * @return 生成的token
     */
    public JsTokenModel createToken(
    		String userId
    		,String tenantId
    		,String userType
    		,List<String> ownerList,String locale,String url) throws Exception {

    	String token = MD5Utils.getMD5(userId);

		JsTokenModel tokenModel = new JsTokenModel(
        		userId,
        		token,
        		tenantId,
        		userType,
        		ownerList,
				locale,url);

        // 存储到redis并设置过期时间
        JedisUtils.setObject(TOKEN_PREFIX + token, tokenModel, AuthConstants.TOKEN_EXPIRES_TIME);
        JedisUtils.set(USER_PREFIX + userId, token, AuthConstants.TOKEN_EXPIRES_TIME);
        logger.info("token超时时间:"+ AuthConstants.TOKEN_EXPIRES_TIME +"s");

        return tokenModel;
    }

	@Override
	public JsTokenModel createTokenModel(JsTokenModel model) throws Exception {
		// 存储到redis并设置过期时间
		JedisUtils.setObject(TOKEN_PREFIX + model.getToken(), model, AuthConstants.TOKEN_EXPIRES_TIME);
		JedisUtils.set(USER_PREFIX + model.getUserId(), model.getToken(), AuthConstants.TOKEN_EXPIRES_TIME);
		logger.info("token超时时间:"+ AuthConstants.TOKEN_EXPIRES_TIME +"s");

		return model;
	}

	/**
     * 获取token模型对象
     * @param token
     * @return 是否有效
     */
    public JsTokenModel getTokenModel(String token) {
		JsTokenModel tokenModel = null;
    	if (token != null) {
    		tokenModel = (JsTokenModel) JedisUtils.getObject(TOKEN_PREFIX + token);
    	}
    	return tokenModel;
    }

    /**
     * 根据用户编号查询token模型
     * @param userId 用户编号
     * @return
     */
    public JsTokenModel getTokenModelByUserId(String userId) {
    	String token = JedisUtils.get(USER_PREFIX + userId);
    	if (token != null) {
    		return getTokenModel(token);
    	}

    	return null;
    }

    /**
     * 检查token是否有效
     * @param token
     * @return 是否有效
     * @throws Exception
     */
    public boolean checkToken(String token) throws Exception {
		JsTokenModel tokenModel = getTokenModel(token);
        return checkToken(tokenModel);
    }

    /**
     * 检查token是否有效
     * @param tokenModel
     * @return 是否有效
     */
	public boolean checkToken(JsTokenModel tokenModel) {
		if (tokenModel != null && tokenModel.isValid()) {
//			String token = tokenModel.getToken();
        	// 验证成功，说明此用户进行了一次有效的操作，延长token的过期时间
//            JedisUtils.setObject(TOKEN_PREFIX + token, tokenModel, AuthConstants.TOKEN_EXPIRES_TIME);
//            JedisUtils.set(USER_PREFIX + tokenModel.getUserId(), token, AuthConstants.TOKEN_EXPIRES_TIME);
//			clearToken(tokenModel);
//			try {
//				createToken(tokenModel.getUserId(),tokenModel.getTenantId(),tokenModel.getUserType(),
//						tokenModel.getOwnerList(),tokenModel.getIdList(),tokenModel.getTownerList(),tokenModel.getTidList());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            return true;
        }

        return false;
	}

    /**
     * 清空token
     * @param token
     */
    public void clearToken(String token) {
		JsTokenModel tokenModel = getTokenModel(token);
    	clearToken(tokenModel);
    }

    /**
     * 清除token
     * @param tokenModel token模型对象
     */
    public void clearToken(JsTokenModel tokenModel) {
    	if (tokenModel != null) {
    		JedisUtils.delObject(TOKEN_PREFIX + tokenModel.getToken());
    		JedisUtils.del(USER_PREFIX + tokenModel.getUserId());
    	}
    }
}
