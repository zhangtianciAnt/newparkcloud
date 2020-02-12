package com.nt.service_PHINE;

import com.nt.dao_PHINE.Vo.FilemarkVo;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE
 * @ClassName: FilemarkService
 * @Description: 文件标记Service接口
 * @Author: SKAIXX
 * @CreateDate: 2020/2/11
 * @Version: 1.0
 */
public interface FilemarkService {

    // 保存文件标记信息
    ApiResult saveFileMarkInfo(TokenModel tokenModel, FilemarkVo filemarkVo);
}
