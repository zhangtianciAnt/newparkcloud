package com.nt.dao_Utils;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class SmsConfig extends Platformsmsconfig implements Serializable {

	private static final long serialVersionUID = 5472582167230465255L;

	/**
	 * mail内容
	 */
	private Map<String, String> content;
	/**
	 * 手机号
	 */
	private String recNum;
	/**
	 * 模板code
	 */
	private String templateCode;
}
