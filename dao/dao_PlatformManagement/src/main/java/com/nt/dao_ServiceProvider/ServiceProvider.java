/**
 * 服务商实体
 */
package com.nt.dao_ServiceProvider;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "serviceProvider")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProvider extends BaseModel {
    private String _id; // 主键
    private String companyName; // 公司名称
    private String contactsName;    //  联系人
    private String contactsPhone;   //  联系人手机号
    private String auditStatus; // 审核状态 0:审批通过，1:未通过
}
