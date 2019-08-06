package com.nt.dao_ServiceProvider;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "onlineApplication")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineApplication extends BaseModel {
    private String _id; // 主键
    private String pmInformationDelivery_id;
    private String serviceProvider_id;
    private String contactsName;    //  联系人
    private String contactsPhone;   //  联系人手机号

    private ServiceProvider serviceProvider;
    private PMInformationDelivery pmInformationDelivery;

}
