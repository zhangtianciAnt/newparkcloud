package com.nt.service_PHINE;

import com.nt.dao_PHINE.Cabinetinfo;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE
 * @ClassName: CabinetinfoService
 * @Description: 机柜信息Service接口
 * @Author: SKAIXX
 * @CreateDate: 2020/2/5
 * @Version: 1.0
 */
public interface CabinetinfoService {

    // 添加机柜信息
    ApiResult saveCabinetInfo(TokenModel tokenModel, Cabinetinfo cabinetinfo);

    // 更新机柜信息
    ApiResult updateCabinetInfo(TokenModel tokenModel, Cabinetinfo cabinetinfo);

    // 删除机柜信息
    ApiResult deleteCabinetInfo(Cabinetinfo cabinetinfo);
}
