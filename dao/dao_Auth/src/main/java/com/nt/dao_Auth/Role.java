package com.nt.dao_Auth;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Auth
 * @ClassName: Role
 * @Description: 角色Dao
 * @Author: SKAIXX
 * @CreateDate: 2018/12/3
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/12/3
 * @UpdateRemark: 新建
 * @Version: 1.0
 */
@Document(collection = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseModel {
    // region properties
    /**
     * 数据主键ID
     */
    private String _id;

    /**
     * 角色名称
     */
    private String rolename;

    /**
     * 角色描述
     */
    private String description;
    /**
     * 默认角色
     */
    private String defaultrole;


    /**
     * 应用权限集合
     */
    private List<AppPermission> apps;

    /**
     * 菜单权限集合
     */
    private List<AppPermission.menu> menus;

    /**
     * 功能权限集合
     */
    private List<AppPermission.menu.actions> actions;
    // endregion
}
