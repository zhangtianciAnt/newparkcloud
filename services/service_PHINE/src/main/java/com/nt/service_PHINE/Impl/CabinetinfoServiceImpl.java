package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Cabinetinfo;
import com.nt.service_PHINE.CabinetinfoService;
import com.nt.service_PHINE.mapper.CabinetinfoMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE.Impl
 * @ClassName: CabinetinfoServiceImpl
 * @Description: 机柜信息Service实现类
 * @Author: SKAIXX
 * @CreateDate: 2020/2/5
 * @Version: 1.0
 */
@Service
public class CabinetinfoServiceImpl implements CabinetinfoService {

    @Autowired
    private CabinetinfoMapper cabinetinfoMapper;

    @Override
    public ApiResult saveCabinetInfo(TokenModel tokenModel, Cabinetinfo cabinetinfo) {
        cabinetinfo.setId(UUID.randomUUID().toString());
        cabinetinfo.preInsert(tokenModel);
        int result = cabinetinfoMapper.insert(cabinetinfo);
        if (result > 0) {
            return ApiResult.success(MsgConstants.INFO_01, cabinetinfo.getId());
        } else {
            return ApiResult.fail(MsgConstants.ERROR_01);
        }
    }

    @Override
    public ApiResult updateCabinetInfo(TokenModel tokenModel, Cabinetinfo cabinetinfo) {
        cabinetinfo.preUpdate(tokenModel);
        int result = cabinetinfoMapper.updateByPrimaryKey(cabinetinfo);
        if (result > 0) {
            return ApiResult.success(MsgConstants.INFO_01);
        } else {
            return ApiResult.fail(MsgConstants.ERROR_01);
        }
    }

    @Override
    public ApiResult deleteCabinetInfo(Cabinetinfo cabinetinfo) {
        int result = cabinetinfoMapper.delete(cabinetinfo);
        if (result > 0) {
            return ApiResult.success(MsgConstants.INFO_01);
        } else {
            return ApiResult.fail(MsgConstants.ERROR_01);
        }
    }
}
