package com.nt.dao_Org;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "tenant")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tenant implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据主键ID
     */
    private String _id;
    private String appid;
    private String appSecret;
    private String tenantid;
    private String description;
}
