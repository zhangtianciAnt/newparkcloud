package com.nt.utils.dao;

import com.nt.utils.AuthConstants;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public abstract class BaseModel implements Serializable{

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

	@Transient
	private List<String> owners;

	@Transient
	private List<String> ids;

    public void preInsert(TokenModel tokenModel){
	    this.createby = tokenModel.getUserId();
	    this.createon = new Date();
	    this.owner = tokenModel.getUserId();
	    this.tenantid = tokenModel.getTenantId();
        this.status = AuthConstants.DEL_FLAG_NORMAL;
    }

    public void preUpdate(TokenModel tokenModel){
	    this.modifyby = tokenModel.getUserId();
        this.modifyon = new Date();
    }
}
