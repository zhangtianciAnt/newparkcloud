package com.nt.service_BASF;

import com.nt.dao_BASF.Emergencytemplate;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:BASF10801Services
 * @Author: WXL
 * @Description:数据监控管理接口
 * @Date: 2019/11/18 17:32
 * @Version: 1.0
 */
public interface EmergencytemplateServices {

    //获取模板列表
    List<Emergencytemplate> list() throws Exception;

    //创建模板
    void insert(Emergencytemplate emergencytemplate, TokenModel tokenModel) throws Exception;

    //删除模板
    void delete(Emergencytemplate emergencytemplate) throws Exception;

    //获取模板据详情
    Emergencytemplate one(String templateid) throws Exception;

    //更新模板数据
    void update(Emergencytemplate emergencytemplate, TokenModel tokenModel) throws Exception;

}
