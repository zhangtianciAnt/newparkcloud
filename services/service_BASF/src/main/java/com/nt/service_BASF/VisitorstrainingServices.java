package com.nt.service_BASF;

import com.nt.dao_BASF.Visitorstraining;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: VisitorstrainingServices
 * @Author:
 * @Description: 访客培训记录接口类
 * @Date: 2019/11/25 10:42
 * @Version: 1.0
 */
public interface VisitorstrainingServices {

    //获取访客培训记录列表
    List<Visitorstraining> list() throws Exception;

    //新增访客培训记录
    void insert(Visitorstraining visitorstraining, TokenModel tokenModel) throws Exception;
}
