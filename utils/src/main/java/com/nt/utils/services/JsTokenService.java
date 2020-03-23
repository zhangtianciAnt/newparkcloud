package com.nt.utils.services;

import com.nt.utils.dao.JsTokenModel;

import java.util.List;


/**
 * 对token进行操作的管理接口
 *
 * @author shenjian
 */
public interface JsTokenService {

    /**
     * 创建一个token关联上指定用户
     *
     * @param userId 指定用户的id
     * @return 生成的token
     */
    JsTokenModel createToken(String userId, String tenantId, String userType, List<String> ownerList, String locale,String url,List<String> roleIds) throws Exception;

    JsTokenModel createTokenModel(JsTokenModel model) throws Exception;

    /**
     * 获取token模型对象
     *
     * @param token
     * @return 是否有效
     */
    JsTokenModel getTokenModel(String token);

    /**
     * 根据用户编号查询token模型
     *
     * @param userId 用户编号
     * @return
     */
    JsTokenModel getTokenModelByUserId(String userId);

    /**
     * 检查token是否有效
     *
     * @param tokenModel
     * @return 是否有效
     */
    boolean checkToken(JsTokenModel tokenModel);

    /**
     * 清除token
     *
     * @param token
     */
    void clearToken(String token);

    /**
     * 清除token
     *
     * @param tokenModel token模型对象
     */
    void clearToken(JsTokenModel tokenModel);
}
