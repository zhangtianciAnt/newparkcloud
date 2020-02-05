package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Machineroominfo;
import com.nt.service_PHINE.MachineroominfoService;
import com.nt.service_PHINE.mapper.MachineroominfoMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE.Impl
 * @ClassName: MachineroominfoServiceImpl
 * @Description: 机房信息Service实现类
 * @Author: SKAIXX
 * @CreateDate: 2020/2/5
 * @Version: 1.0
 */
@Service
public class MachineroominfoServiceImpl implements MachineroominfoService {

    @Autowired
    private MachineroominfoMapper machineroominfoMapper;

    @Override
    public ApiResult saveMachineRoomInfo(TokenModel tokenModel, Machineroominfo machineroominfo) {
        machineroominfo.setId(UUID.randomUUID().toString());
        machineroominfo.preInsert(tokenModel);
        int result = machineroominfoMapper.insert(machineroominfo);
        if (result > 0) {
            return ApiResult.success(MsgConstants.INFO_01, machineroominfo.getId());
        } else {
            return ApiResult.fail(MsgConstants.ERROR_01);
        }
    }

    @Override
    public ApiResult updateMachineRoomInfo(TokenModel tokenModel, Machineroominfo machineroominfo) {
        machineroominfo.preUpdate(tokenModel);
        int result = machineroominfoMapper.updateByPrimaryKey(machineroominfo);
        if (result > 0) {
            return ApiResult.success(MsgConstants.INFO_01);
        } else {
            return ApiResult.fail(MsgConstants.ERROR_01);
        }
    }

    @Override
    public ApiResult deleteMachineRoomInfo(Machineroominfo machineroominfo) {
        int result = machineroominfoMapper.delete(machineroominfo);
        if (result > 0) {
            return ApiResult.success(MsgConstants.INFO_01);
        } else {
            return ApiResult.fail(MsgConstants.ERROR_01);
        }
    }
}
