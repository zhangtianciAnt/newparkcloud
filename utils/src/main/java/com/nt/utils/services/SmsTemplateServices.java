package com.nt.utils.services;

import com.nt.dao_Utils.Platformsmsteplate;

import java.util.List;

public interface SmsTemplateServices {

	/**
	 * 获取短信模板列表
	 * @return
	 */
	public List<Platformsmsteplate> selectSmsTemplateList() throws Exception;

	/**
	 * 根据模板id获取短信模板
	 * @param tempCode 模板code
	 * @return
	 */
	public Platformsmsteplate selectSmsTemplateByTempCode(String tempCode) throws Exception;

	/**
	 * 获取短信模板
	 * @param tempId 配置id
	 * @return
	 */
	public Platformsmsteplate selectSmsTemplate(String tempId) throws Exception;

	/**
	 * 插入短信模板
	 * @param pst
	 */
	public void insertSmsTemplate(Platformsmsteplate pst) throws Exception;

	/**
	 * 更新短信模板
	 * @param userId 用户id
	 * @param pst
	 */
	public void updateSmsTemplate(Platformsmsteplate pst, String userId) throws Exception;

	/**
	 * 更新短信模板状态
	 * @param tempId 配置id
	 * @param userId 用户id
	 */
	public void updateSmsTemplateStatus(String tempId, String userId) throws Exception;

}
