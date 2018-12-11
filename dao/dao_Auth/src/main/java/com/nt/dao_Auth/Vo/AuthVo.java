package com.nt.dao_Auth.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Auth
 * @ClassName: AppVo
 * @Description: 应用和菜单Vo
 * @Author: WENCHAO
 * @CreateDate: 2018/12/11
 * @UpdateUser: WENCHAO
 * @UpdateDate: 2018/12/11
 * @UpdateRemark: 新建
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthVo {
    /**
     * 用户权限内的应用id集合
     */
    private List<String> appids;
    /**
     * 用户权限内的菜单id集合
     */
    private List<String> menuids;
}
