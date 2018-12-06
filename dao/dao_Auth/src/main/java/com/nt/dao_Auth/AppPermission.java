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
 * @ClassName: AppPermission
 * @Description: 权限Dao
 * @Author: SKAIXX
 * @CreateDate: 2018/12/3
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/12/3
 * @UpdateRemark: 新建
 * @Version: 1.0
 */
@Document(collection = "apppermission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppPermission extends BaseModel {
    // region properties
    /**
     * 数据主键ID
     */
    private String _id;

    /**
     * 应用名称
     */
    private String appname;

    /**
     * 图标
     */
    private String appicon;

    /**
     * 价格
     */
    private String appprice;

    /**
     * 应用描述
     */
    private String appdescription;

    /**
     * 菜单权限
     */
    private List<menu> menus;

    // region 菜单权限
    @Data
    public static class menu extends BaseModel {
        // region properties
        /**
         * 菜单ID
         */
        private String _id;

        /**
         * 菜单名称
         */
        private String name;

        /**
         * 菜单路径
         */
        private String menuurl;

        /**
         * 菜单图标
         */
        private String menuicon;

        /**
         * 是否表示
         */
        private String menuvisible;

        /**
         * 表示顺序
         */
        private String menuorderno;

        /**
         * 功能权限
         */
        private List<actions> actions;

        /**
         * 子菜单
         */
        private List<menu> children;

        // region 功能权限
        @Data
        public static class actions extends BaseModel {
            // region properties
            /**
             * 主键
             */
            private String _id;
            /**
             * 功能类型
             */
            private String actiontype;

            /**
             * 功能名称
             */
            private String name;

            /**
             * 数据权限
             */
            private int auth;

            /**
             * 数据权限
             */
            private List<permissions> permissions;

            // region 数据权限
            @Data
            public static class permissions extends BaseModel {
                // region properties
                /**
                 * 权限类型
                 */
                private String permissiontype;

                /**
                 * 权限名称
                 */
                private String permissionname;
                // endregion
            }
            // endregion
            // endregion
        }
        // endregion
        // endregion
    }
    // endregion
    // endregion
}
