package com.nt.dao_Org.Vo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: PersonalCenterVo
 * @Description: 基本信息
 * @Author: FEIJIALIANG
 * @CreateDate: 2018/12/05
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalCenterVo {
    /**
     * 用户信息
     */
    private UserAccount userAccount;
    /**
     * 客户信息
     */
    private CustomerInfo customerInfo;
}