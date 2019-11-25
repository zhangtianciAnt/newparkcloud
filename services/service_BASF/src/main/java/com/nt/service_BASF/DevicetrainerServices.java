package com.nt.service_BASF;


import com.nt.dao_BASF.Devicetrainer;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:BASF21203Services
 * @Author: WXL
 * @Description:数据监控管理接口
 * @Date: 2019/11/21 13:34
 * @Version: 1.0
 */
public interface DevicetrainerServices {

    //获取培训项目列表
    List<Devicetrainer> list() throws Exception;

    //创建培训项目
    void insert(Devicetrainer devicetrainer, TokenModel tokenModel) throws Exception;

    //删除培训项目
    void delete(Devicetrainer devicetrainer) throws Exception;

    //获取培训项目据详情
    Devicetrainer one(String devicetrainerid) throws Exception;

    //更新课程数据
    void update(Devicetrainer devicetrainer, TokenModel tokenModel) throws Exception;

}
