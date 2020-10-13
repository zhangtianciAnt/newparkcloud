package com.nt.service_BASF;

import com.nt.dao_BASF.Environment;
import com.nt.dao_BASF.Pimspoint;
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
    List<Pimspoint> list() throws Exception;

    //获取监控数据详情
    Pimspoint one(String pimspointid) throws Exception;

    //更新监控数据
    void update(Pimspoint pimspoint, TokenModel tokenModel) throws Exception;

}
