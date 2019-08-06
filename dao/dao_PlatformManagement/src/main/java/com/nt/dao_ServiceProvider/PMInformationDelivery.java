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
    private String cover;  // 封面
    private String explain;       // 说明
    private String publicationStatus;   //  发布状态
    private String auditStatus; //  审核状态
    private ServiceProvider serviceProvider;   //服务商信息
}
