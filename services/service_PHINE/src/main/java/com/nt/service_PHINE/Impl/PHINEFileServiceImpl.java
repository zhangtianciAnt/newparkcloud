package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.service_PHINE.PHINEFileService;
import com.nt.service_PHINE.mapper.FileinfoMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PHINEFileServiceImpl implements PHINEFileService {

    @Autowired
    private FileinfoMapper fileinfoMapper;

    @Override
    public ApiResult saveFileInfo(TokenModel tokenModel, Fileinfo fileInfo) throws Exception {
        fileInfo.setId(UUID.randomUUID().toString());
        fileInfo.preInsert(tokenModel);
        int result = fileinfoMapper.insert(fileInfo);
        if (result > 0) {
            return ApiResult.success(MsgConstants.INFO_01, fileInfo.getId());
        } else {
            return ApiResult.fail(MsgConstants.ERROR_01);
        }
    }
}
