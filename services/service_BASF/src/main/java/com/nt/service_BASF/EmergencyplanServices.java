package com.nt.service_BASF;

import com.nt.utils.dao.TokenModel;
import com.nt.dao_BASF.Emergencyplan;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: EmergencyplanServices
 * @Author: Y
 * @Description: EmergencyplanServices
 * @Date: 2019/11/18 17:11
 * @Version: 1.0
 */
public interface EmergencyplanServices {

    //获取应急预案
    List<Emergencyplan> list() throws Exception;

    //创建应急预案
    void insert(Emergencyplan emergencyplan, TokenModel tokenModel) throws Exception;

    //删除应急预案
    void delete(Emergencyplan emergencyplan) throws Exception;

    //获取应急预案详情
    Emergencyplan one(String emergencyplanid) throws Exception;

    //更新应急预案数据
    void update(Emergencyplan emergencyplan, TokenModel tokenModel) throws Exception;

}
