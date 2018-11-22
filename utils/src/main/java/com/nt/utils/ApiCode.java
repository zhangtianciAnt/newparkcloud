package com.nt.utils;

/**
 * 接口返回码
 *
 * 命名规则： 错误类型码(1位) + 服务模块代码(2位) + 具体错误代码(2位)
 * 详细说明：
 *      1. 错误类型码有2种：
 *          a. 系统级别错误：主要是描述一些系统级别的错误，如：系统错误，服务不可用，服务超时，接口不存在，ip限制和缺少参数等。
 *          b. 服务级别错误：主要是描述服务和业务级别的错误，如：参数为空，非法数据，无效的参数，不合法的城市代码等。
 *      2. 服务模块代码：
 *          00 表示通用服务
 *          01 表示系统服务
 *          其余的可根据编号来自定义
 *      3. 具体错误码：见定义详情
 * 额外说明：
 *      0 表示成功
 *
 * @author shenjian
 */
public enum ApiCode {
    // 系统级别错误
    CUSTOM_ERROR_TYPE(10000, "自定义错误"),
    SYSTEM_ERROR(10001, "系统错误"),
    SERVICE_UNAVAILABLE(10002, "服务已停止使用"),
    MISS_PARAMETER(10002, "缺失必选参数，请查看API文档"),
    NETWORK_BUSY(10003, "网络繁忙"),
    UNAUTHORIZED(10004, "无权限"),
    OBJECT_NOT_EXISTS(10005, "对象不存在"),

    // 服务级别错误 - 通用服务
    PARAMETER_ERROR(20001, "参数错误"),

    // 服务级别错误 - 系统服务
    USER_NOT_LOGIN(20101, "用户未登录"),
    // 服务级别错误 - 钱包服务
    WALLET_DEDUCT_ERROR(20200, "系统出错，扣款异常"),
    SECURITY_KEY_ERROR(20201, "签名错误"),
    OWNER_ERROR(20202, "车主不存在"),
    PET_CARD_ERROR(20203, "储值卡不存在"),
    WALLET_ERROR(20204, "虚拟卡不存在"),
    WALLET_STATUS_ERROR(20205, "虚拟卡暂不能使用"),
    WALLET_BALANCE_ENOUGH(20206, "余额不足"),
    DEDUCTION_REPEAT(20207, "重复扣款请求"),
    INSERT_REQUEST_RECORD(20208, "保存扣款请求错误"),

    SUCCESS(0, "操作成功");

    private final int code;
    private final String message;

    private ApiCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
