package com.nt.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonFormat;

public abstract class BaseModel<T> implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	private List<String> ownerList;

	@Transient
	private List<String> idList;

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


}
