package com.nt.service_PHINE;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

public interface PHINEFileService {
    ApiResult saveFileInfo(TokenModel tokenModel, Fileinfo fileInfo) throws Exception;
}
