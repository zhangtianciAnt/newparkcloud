package com.nt.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.impl.TokenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class RequestUtils {
	private static Logger log = LoggerFactory.getLogger(RequestUtils.class);
	 private static final String OFFSET    = "offset";
	 private static final String LIMITE    = "limit";
	/**
	 * 获取当前用户
	 *
	 * @param request
	 * @return String
	 */
	public static String CurrentUserId(HttpServletRequest request) throws Exception {
			TokenServiceImpl tokenService = new TokenServiceImpl();
			TokenModel tokenModel = tokenService.getToken(request);
			return tokenModel.getUserId();
	}

	/**
	 * 获取当前用户类型
	 *
	 * @param request
	 * @return String
	 */
	public static String CurrentUserType(HttpServletRequest request) throws Exception {
		String token = request.getHeader(AuthConstants.AUTH_TOKEN);
		if(StringUtils.isEmpty(token)){
			return "";
		}else{
			TokenServiceImpl tokenService = new TokenServiceImpl();
			TokenModel tokenModel = tokenService.getToken(request);
			return tokenModel.getUserType();
		}
	}

	/**
	 * 获取当前用户租户ID
	 *
	 * @param request
	 * @return String
	 */
	public static String CurrentTenantId(HttpServletRequest request) throws Exception {
		String token = request.getHeader(AuthConstants.AUTH_TOKEN);
		if(StringUtils.isEmpty(token)){
			return "";
		}else{
			TokenServiceImpl tokenService = new TokenServiceImpl();
			TokenModel tokenModel = tokenService.getToken(request);
			return tokenModel.getTenantId();
		}
	}

	/**
	 * 获取负责人列表
	 *
	 * @param request
	 * @return List<String>
	 */
	public static List<String> getOwnerList(HttpServletRequest request) throws Exception {
		String token = request.getHeader(AuthConstants.AUTH_TOKEN);
		if(StringUtils.isEmpty(token)){
			return new ArrayList<String>();
		}else{
			TokenServiceImpl tokenService = new TokenServiceImpl();
			TokenModel tokenModel = tokenService.getToken(request);
			JSONObject jsonObject = JSONUtil.parseObj(tokenModel);
			log.error(jsonObject.toString());
            return tokenModel.getOwnerList();
		}
	}
	/**
	 * 获取主键列表
	 *
	 * @param request
	 * @return String
	 */
	public static List<String> getIdList(HttpServletRequest request) throws Exception {
		String token = request.getHeader(AuthConstants.AUTH_TOKEN);
		if(StringUtils.isEmpty(token)){
			return new ArrayList<String>();
		}else{
			TokenServiceImpl tokenService = new TokenServiceImpl();
			TokenModel tokenModel = tokenService.getToken(request);
            return tokenModel.getIdList();
		}
	}

	/**
	 * 设置权限列表
	 *
	 * @param request
	 * @return String
	 */
	public static <T extends BaseModel> T CurrentPageOwnerList(HttpServletRequest request, T record) throws Exception {
		String token = request.getHeader(AuthConstants.AUTH_TOKEN);
		if(!StringUtils.isEmpty(token)){
			TokenServiceImpl tokenService = new TokenServiceImpl();
			TokenModel tokenModel = tokenService.getToken(request);
			record.setOwners(tokenModel.getOwnerList());
			record.setIds(tokenModel.getIdList());
		}else{
			record.setOwners(new ArrayList<String>());
			record.setIds(new ArrayList<String>());
		}
		return record;
	}
}
