package com.nt.dao_ServiceProvider;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pmInformationDelivery")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PMInformationDelivery extends BaseModel {
    private String _id; // 主键
    private String title;   // 标题啊
    private String typeName;    // 类别
    private String imagespath;  // 封面地址
    private String explain;       // 说明
    private String publicationStatus;   //  发布状态
    private String auditStatus; //  审核状态
    private ServiceProvider serviceProvider;   //服务商信息

    @Data
    public static class ServiceProvider {
        private String _id; // 主键
        private String companyName; // 公司名称
        private String contactsName;    //  联系人
        private String contactsPhone;   //  联系人手机号
        private String auditStatus; // 审核状态 0:审批通过，1:未通过
    }

}
