package com.nt.service_PHINE;

import com.nt.dao_PHINE.Machineroominfo;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE
 * @ClassName: MachineroominfoService
 * @Description: 机房信息Service接口
 * @Author: SKAIXX
 * @CreateDate: 2020/2/5
 * @Version: 1.0
 */
public interface MachineroominfoService {

    // 添加机房信息
    ApiResult saveMachineRoomInfo(TokenModel tokenModel, Machineroominfo machineroominfo);

    // 更新机房信息
    ApiResult updateMachineRoomInfo(TokenModel tokenModel, Machineroominfo machineroominfo);

    // 删除机房信息
    ApiResult deleteMachineRoomInfo(Machineroominfo machineroominfo);
}
