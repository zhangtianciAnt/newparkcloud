package com.nt.dao_Org;

import com.nt.utils.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: Tenant
 * @Description: 租户
 * @Author: SKAIXX
 * @CreateDate: 2018/10/25
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/10/25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Document(collection = "tenant")
@Data
public class Tenant extends BaseModel {
    // region properties
    /**
     * 数据主键ID
     */
    private String _id;
    /**
     * 租户ID
     */
    private String tenantid;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    // endregion
}
