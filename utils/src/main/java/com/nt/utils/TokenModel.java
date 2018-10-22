package com.nt.utils;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 基于Token认证的实体模型
 * 
 * @author shenjian
 */
@Document(collection = "login")
public class TokenModel implements Serializable {

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
		
	// ID列表
	private List<String> idList;

    private Date date;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(List<String> ownerList) {
        this.ownerList = ownerList;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public TokenModel(String userId, String token,
                      String tenantId, String userType,
                      List<String> ownerList,List<String> idList) {
        this.userId = userId;
        this.token = token;
        this.tenantId = tenantId;
        this.userType = userType;
        this.ownerList = ownerList;
        this.idList = idList;
    }
}
