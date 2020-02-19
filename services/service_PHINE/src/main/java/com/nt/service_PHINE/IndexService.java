package com.nt.service_PHINE;

import com.nt.dao_PHINE.Vo.DashboardInfoVo;
import com.nt.utils.dao.TokenModel;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE
 * @ClassName: IndexService
 * @Description: java接口的作用描述
 * @Author: SKAIXX
 * @CreateDate: 2020/2/19
 * @Version: 1.0
 */
public interface IndexService {

    // 获取仪表盘数据
    DashboardInfoVo getDashboardInfo(TokenModel tokenModel);
}
