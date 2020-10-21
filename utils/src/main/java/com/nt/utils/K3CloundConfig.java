package com.nt.utils;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class K3CloundConfig {

    /**
     * 金蝶url前缀
     */
    public final static String url = "http://59.46.229.242:8090/k3cloud/";

    /**
     * 登录接口
     */
//    private String login;
    public final static String login = "Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc";

    /**
     * 保存接口
     */
//    private String save;
    public final static String save = "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Save.common.kdsvc";
    /**
     * 批量保存接口
     */
//    private String save;
    public final static String batchSave = "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.BatchSave.common.kdsvc";

}
