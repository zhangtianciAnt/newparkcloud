package com.nt.utils;

/**
 * 常量类
 *
 * @author shenjian
 */
public class AuthConstants {

    // 请求头中的用来标记token的名称
    public final static String AUTH_TOKEN = "x-auth-token";

    //内部公司
    public static final String IN_COMPANY = "1";

    //内部用户
    public static final String IN_USER = "2";

    //外部公司
    public static final String OUT_COMPANY = "3";

    //外部用户
    public static final String OUT_USER = "4";
    /**
     * 删除标记（0：正常；1：删除；2：审核；3:锁定）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";
    public static final String DEL_FLAG_LOCK = "3";


    public static final String TENANTID = "tenantid";
    public static final String OWNER = "owner";
    public static final String OWNERS = "owners";

    public static final String APPLYTENANTID = "APPLYTENANTID";

    public static final String LOG_TYPE_LOGIN = "1";
    public static final String LOG_TYPE_OPERATION = "2";

    public static final String LOG_EQUIPMENT_PC = "PC";
    public static final String LOG_EQUIPMENT_APP = "APP";
    public static final String LOG_EQUIPMENT_API = "API";
}
