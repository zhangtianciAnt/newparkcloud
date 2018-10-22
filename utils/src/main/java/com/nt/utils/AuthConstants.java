package com.nt.utils;

/**
 * 常量类
 * 
 * @author shenjian
 */
public class AuthConstants {
	
	// token过期时间，默认60分钟
	public final static int TOKEN_EXPIRES_TIME = 3600;
	
	// 请求头中的用来标记token的名称
	public final static String AUTH_TOKEN = "x-auth-token";
	
	// 请求头中的用来标记menu的名称
	public final static String AUTH_MENUCODE = "x-auth-menucode";
	
	// 注入request中的属性名
	public final static String CURRENT_USER_ID = "current_user_id";
	
	// shiro会话存储前缀
	public final static String SHIRO_SESSION_PREFIX = "dlsp_session_";
	
	// shiro授权缓存存储前缀
	public final static String SHIRO_CACHE_PREFIX = "dlsp_cache_";
	
    // 用户权限前缀
    public final static String USER_PRIVILEGE_PREFIX = "user_privilege"; 
    
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
    public static final String DEL_FLAG_AUDIT  = "2";
    
    public static final String DEL_FLAG_LOCK = "3";
    /**
     * 登录日志标题
     */
    public static final String LOGIN_LOG_NEME = "登录";
    /**
     * 登录日志取得范围(月份)
     */
    public static final int LOGIN_LOG_RANGE = -6;
    /**
     * 消息开关状态(0:启用;1:禁用)
     */
    public static final String MESSAGE_SWITCH_ON = "0";
    public static final String MESSAGE_SWITCH_OFF = "1";
    /**
     * 登录用户类型(0:平台;1:租户;2:用户)
     */
    public static final String USER_TYPE_PLATFORM = "0";
    public static final String USER_TYPE_TENANT = "1";
    public static final String USER_TYPE_USER = "2";
    /**
     * 资讯状态(0：草稿，1：审核中，2：发布，3：禁用)
     */
    public static final String ANNO_FLAG_DRAFT = "0";
    public static final String ANNO_FLAG_AUDIT = "1";
    public static final String ANNO_FLAG_PUBLISH = "2";
    public static final String ANNO_FLAG_LOCK = "3";
    /**
     * 消息方式(0:邮件;1:短信;2:微信)
     */
    public static final String MESSAGE_WAY_MAIL = "0";
    public static final String MESSAGE_WAY_SMS = "1";
    public static final String MESSAGE_WAY_WECHAT = "2";
    /**
     * 流程审批状态(0:未审批，1:已审批)
     */
    public static final String APPROVED_FLAG_NO = "0";
    public static final String APPROVED_FLAG_YES = "1";
    /**
     * 流程审批结果(0-同意;1-退回;2-查看;3-循环同意;4-循环退回)
     */
    public static final String RESULT_TYPE_AGREE = "0";
    public static final String RESULT_TYPE_REBUT = "1";
    public static final String RESULT_TYPE_SEE = "2";
    public static final String RESULT_TYPE_LOOP_AGREE = "3";
    public static final String RESULT_TYPE_LOOP_REBUT = "4";
    /**
     * 流程实例状态(0-已开启;1-完成;2-驳回;3-删除)
     */
    public static final String WORK_FLAG_OPEN = "0";
    public static final String WORK_FLAG_FINISH = "1";
    public static final String WORK_FLAG_NO = "2";
    public static final String WORK_FLAG_DEL = "3";
    /**
     * 任务/通知类型(0：通知，1：任务)
     */
    public static final String TASK_TYPE_NOTICE = "0";
    public static final String TASK_TYPE_TASK = "1";
    /**
     * 邮件模板类型(0：找回密码，1：变更邮箱)
     */
    public static final String TEMPLATE_TYPE_FIND = "0";
    public static final String TEMPLATE_TYPE_CHANGE = "1";
    /**
     * 邮件模板code
     */
    public static final String TEMPLATE_CODE_FIND = "T000000001";
    public static final String TEMPLATE_CODE_CHANGE = "T000000002";
    /**
     * 邮件title
     */
    public static final String MAIL_TITLE = "安全验证";
    /**
     * 通知读取状态(0：未读，1：已读)
     */
    public static final String TASK_UNREAD = "0";
    public static final String TASK_HAVEREAD = "1";

    public static final  String TENANTID ="tenantid";
    public static final  String OWNER ="owner";
    public static final  String OWNERS ="owners";
}
