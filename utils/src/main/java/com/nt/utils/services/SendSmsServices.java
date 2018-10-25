package com.nt.utils.services;

import com.nt.dao_Utils.SmsConfig;

public interface SendSmsServices {

	/**
	 * 发送短信
	 * @param smsConfig
	 * @throws Exception
	 */
	public void sendMessage(SmsConfig smsConfig) throws Exception;
}
