package com.nt.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nt.utils.dao.TokenModel;
import lombok.Data;
import org.springframework.data.annotation.Transient;
@Data
public abstract class BaseModel<T> implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 状态
	 */
	private String status;
	/**
	 * 创建时间
	 */
	private Date createon;
	/**
	 * 创建者
	 */
	private String createby;
	/**
	 * 更新时间
	 */
	private Date modifyon;
	/**
	 * 更新者
	 */
	private String modifyby;
	/**
	 * 负责人
	 */
	private String owner;
	/**
	 * 租户id
	 */
	private String tenantid;

	@org.springframework.data.annotation.Transient
	private List<String> owners;

	@org.springframework.data.annotation.Transient
	private List<String> ids;

	@org.springframework.data.annotation.Transient
	private Integer currentPage;

	@org.springframework.data.annotation.Transient
	private Integer pageSize;

	@Transient
	private String httpOriginType;

	public void preInsert(){
		this.createon = new Date();
		this.status = AuthConstants.DEL_FLAG_NORMAL;
	}

	public void preInsert(TokenModel tokenModel){
		this.createby = tokenModel.getUserId();
		this.createon = new Date();
		if(StrUtil.isEmpty(this.owner)){
			this.owner = tokenModel.getUserId();
		}
		this.tenantid = tokenModel.getTenantId();
		this.status = AuthConstants.DEL_FLAG_NORMAL;
	}

	public void preUpdate(TokenModel tokenModel){
		this.modifyby = tokenModel.getUserId();
		this.modifyon = new Date();
	}


}
