package com.nt.dao_Org.Vo;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: CustomerInfo UserAccount
 * @Description: 用户管理VO
 * @Author: ZHANGYING
 * @CreateDate: 2018/12/06
 * @UpdateUser: ZHANGYING
 * @UpdateDate: 2018/12/06
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo extends BaseModel {
    private UserAccount userAccount;

    private CustomerInfo customerInfo;
}

