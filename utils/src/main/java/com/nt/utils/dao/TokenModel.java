package com.nt.utils.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 基于Token认证的实体模型
 *
 * @author shenjian
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "login")
public class TokenModel implements Serializable {

	//主键
	private String _id;

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

    private Date expireDate;

	private String locale;
}
