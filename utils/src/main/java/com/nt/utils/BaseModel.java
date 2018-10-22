package com.nt.utils;

import org.springframework.data.annotation.Transient;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateon() {
		return createon;
	}

	public void setCreateon(Date createon) {
		this.createon = createon;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public Date getModifyon() {
		return modifyon;
	}

	public void setModifyon(Date modifyon) {
		this.modifyon = modifyon;
	}

	public String getModifyby() {
		return modifyby;
	}

	public void setModifyby(String modifyby) {
		this.modifyby = modifyby;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        this.owners = owners;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

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
