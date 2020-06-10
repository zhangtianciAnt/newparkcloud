package com.nt.service_Org;

import com.nt.dao_Org.CustomerInfo;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_Org
 * @ClassName: 企业微信Services
 * @Description: java类作用描述
 * @Author: YANGSHUBO
 * @CreateDate: 2020/06/08
 * @UpdateUser: YANGSHUBO
 * @UpdateDate: 2020/06/08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface EWechatService {

    /**
     * 微信userId登录
     *
     * @param access_token 微信userId
     * @return
     * @throws Exception
     */

    String ewxLogin(String access_token) throws Exception;

    List<CustomerInfo> useridList() throws Exception ;

}
