package com.nt.service_PHINE;

import com.nt.dao_PHINE.Cabinetinfo;
import com.nt.dao_PHINE.Machineroominfo;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

import java.util.List;

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

    // 获取指定机房中的机柜列表
    List<Cabinetinfo> getCabinetinfoListByMachineroomid(String machineroomid);

    // 获取机柜信息
    List<Cabinetinfo> getCabinetInfo(Cabinetinfo cabinetinfo);
}
