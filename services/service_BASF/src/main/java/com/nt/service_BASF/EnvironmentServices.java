package com.nt.service_BASF;

import com.nt.dao_BASF.Environment;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:BASF10201Services
 * @Author: WXL
 * @Description:数据监控管理接口
 * @Date: 2019/11/14 15:32
 * @Version: 1.0
 */
public interface EnvironmentServices {

    //获取监控列表
    List<Environment> list() throws Exception;

    //创建监控数据
    void insert(Environment environment, TokenModel tokenModel) throws Exception;

    //删除监控数据
    void delete(Environment environment) throws Exception;

    //获取监控数据详情
    Environment one(String environmentid) throws Exception;

    //更新监控数据
    void update(Environment environment, TokenModel tokenModel) throws Exception;

}
