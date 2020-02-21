package com.nt.service_PHINE;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Filemark2file;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PHINEFileService {
    ApiResult saveFileInfo(TokenModel tokenModel, List<Fileinfo> filesInfo, String projectId) throws Exception;

    ApiResult getFileByProjectId(String projectId) throws Exception;

    ApiResult getFileMarkByProjectId(String projectId) throws Exception;

    ApiResult getFilesByFileMarkId(String projectId) throws Exception;

    ApiResult updateFileNameById(TokenModel tokenModel, Fileinfo fileinfo) throws Exception;

    ApiResult delFileMark2File(List<Filemark2file> filemark2fileList) throws Exception;

    ApiResult getLogicLoadHistory(String projectId) throws Exception;

    ApiResult isExistSameNameFile(List<Fileinfo> fileinfoList) throws Exception;
}
