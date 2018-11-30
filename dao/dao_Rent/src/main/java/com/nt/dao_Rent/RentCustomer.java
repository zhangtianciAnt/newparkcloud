package com.nt.dao_Rent;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Rent
 * @ClassName: RentCustomer
 * @Description: 租赁客户DAO
 * @Author: SKAIXX
 * @CreateDate: 2018/11/30
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/11/30
 * @UpdateRemark: 新建
 * @Version: 1.0
 */
@Document(collection = "rentcustomer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentCustomer extends BaseModel {
    //region properties
    /**
     * 数据主键ID
     */
    private String _id;

    /**
     * 客户名称
     */
    private String customername;

    /**
     * 客户类型（1：个人；2：企业；3：企事业单位）
     */
    private String customertype;
    //endregion
}
