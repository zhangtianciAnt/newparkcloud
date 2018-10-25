package com.nt.utils.services;

import com.nt.dao_Utils.Platformsmsconfig;

import java.util.List;

public interface SmsConfigServices {

	/**
	 * 获取短信配置列表
	 * @return
	 */
	public List<Platformsmsconfig> selectSmsConfigList() throws Exception;

	/**
	 * 获取短信配置
	 * @param configId 配置id
	 * @return
	 */
	public Platformsmsconfig selectSmsConfig(String configId) throws Exception;

	/**
	 * 插入短信配置
	 * @param psc
	 */
	public void insertSmsConfig(Platformsmsconfig psc) throws Exception;

	/**
	 * 更新短信配置
	 * @param psc
	 * @param userId 用户id
	 */
	public void updateSmsConfig(Platformsmsconfig psc, String userId) throws Exception;

	/**
	 * 更新短信配置状态
	 * @param configId 配置id
	 * @param userId 用户id
	 */
	public void updateSmsConfigStatus(String configId, String userId) throws Exception;

}
