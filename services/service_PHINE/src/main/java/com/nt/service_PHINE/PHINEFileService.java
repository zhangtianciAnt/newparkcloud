package com.nt.service_PHINE;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PHINEFileService {
    ApiResult saveFileInfo(TokenModel tokenModel, List<Fileinfo> filesInfo, String projectId) throws Exception;
}
