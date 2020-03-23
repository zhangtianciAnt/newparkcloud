package com.nt.utils.dao;

import com.nt.utils.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 基于Token认证的实体模型
 *
 * @author shenjian
 */
public class JsTokenModel implements Serializable {

	private static final long serialVersionUID = 1L;

	// 用户编号
	private String userId;

	// 用户类型
	private String userType;

	// 随机生成的uuid
	private String token;

	// 租户Id
	private String tenantId;

	// 负责人列表
	private List<String> ownerList;

	private String locale;

	private String url;

	// 负责人列表
	private String roleIds;

	public JsTokenModel(String userId, String token,
			String tenantId, String userType,List<String> ownerList,String locale,String url,List<String> roleIds) {
		this.userId = userId;
		this.token = token;
		this.tenantId = tenantId;
		this.userType = userType;
		this.ownerList = ownerList;
		this.locale = locale;
		this.url = url;
		this.roleIds = StringUtils.join(roleIds,",");
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<String> getOwnerList() {
		return ownerList;
	}

	public void setOwnerList(List<String> ownerList) {
		this.ownerList = ownerList;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getRoleIds() {
		if(roleIds == null){
			return null;
		}
		String[] strs=roleIds.split(",");
		List list= Arrays.asList(strs);
		return list;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = StringUtils.join(roleIds,",");
	}

	/**
	 * 检测token是否有效
	 *
	 * @return
	 */
	public boolean isValid() {
		// TODO : 可以增加字段提高安全性，例如时间戳、ip、url签名等
		return token != null;
	}
}
