package com.nt.utils;

/**
 * 常量类
 *
 * @author shenjian
 */
public class AuthConstants {

    // 请求头中的用来标记token的名称
    public final static String AUTH_TOKEN = "x-auth-token";
    public final static String LOCALE = "locale";

    // 请求头中的用来标记当前画面的名称
    public final static String CURRENTURL = "currentUrl";

    //内部公司
    public static final String IN_COMPANY = "1";

    //内部用户
    public static final String IN_USER = "2";

    //外部公司
    public static final String OUT_COMPANY = "3";

    //外部用户
    public static final String OUT_USER = "4";
    /**
     * 删除标记（0：正常；1：删除）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";

    public static final String TENANTID = "tenantid";
    public static final String OWNER = "owner";
    public static final String OWNERS = "owners";
    public static final String HTTPORIGINTYPE = "httpOriginType";

    public static final String APPLYTENANTID = "APPLYTENANTID";

    public static final String LOG_TYPE_LOGIN = "1";
    public static final String LOG_TYPE_OPERATION = "2";

    public static final String LOG_EQUIPMENT_PC = "PC";
    public static final String LOG_EQUIPMENT_APP = "APP";
    public static final String LOG_EQUIPMENT_API = "API";

    public static final String TODONOTICE_TYPE_TODO = "1";
    public static final String TODONOTICE_TYPE_NOTICE = "2";

    public static final String TODO_STATUS_TODO = "0";
    public static final String TODO_STATUS_DELETE = "2";
    public static final String TODO_STATUS_DONE = "1";
    public static final String TODO_STATUS_BACK = "9";

    public static final String NOTICE_STATUS_TOREAD = "0";
    public static final String NOTICE_STATUS_READED = "1";

    /**
     * 流程实例状态(0-已开启;1-完成;2-驳回;3-删除)
     */
    public static final String WORK_FLAG_OPEN = "0";
    public static final String WORK_FLAG_FINISH = "1";
    public static final String WORK_FLAG_NO = "2";
    public static final String WORK_FLAG_DEL = "3";

    /**
     * 流程审批状态(0:未审批，1:已审批)
     */
    public static final String APPROVED_FLAG_NO = "0";
    public static final String APPROVED_FLAG_YES = "4";
}
