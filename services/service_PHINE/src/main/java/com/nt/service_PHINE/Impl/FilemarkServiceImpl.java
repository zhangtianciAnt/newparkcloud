package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Filemark;
import com.nt.dao_PHINE.Filemark2file;
import com.nt.dao_PHINE.Vo.FilemarkVo;
import com.nt.service_PHINE.FilemarkService;
import com.nt.service_PHINE.mapper.Filemark2fileMapper;
import com.nt.service_PHINE.mapper.FilemarkMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE.Impl
 * @ClassName: FilemarkServiceImpl
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2020/2/11
 * @Version: 1.0
 */
@Service
public class FilemarkServiceImpl implements FilemarkService {

    @Autowired
    private FilemarkMapper filemarkMapper;

    @Autowired
    private Filemark2fileMapper filemark2fileMapper;

    /**
     * @return
     * @Method saveFileMarkInfo
     * @Author SKAIXX
     * @Description TODO 方法描述
     * @Date 2020/2/11 12:49
     * @Param
     **/
    @Override
    public ApiResult saveFileMarkInfo(TokenModel tokenModel, FilemarkVo filemarkVo) {
        // TODO:判断当前文件是否已经被标记
        // Filemark Insert
        Filemark filemark = new Filemark();
        filemark.setId(UUID.randomUUID().toString());
        filemark.setProjectid(filemarkVo.getProjectid());
        filemark.setVersion(filemarkVo.getVersion());
        filemark.setVersiondescribtion(filemarkVo.getVersiondescribtion());
        filemark.preInsert(tokenModel);
        filemarkMapper.insert(filemark);

        // Filemark2file Insert
        for (Filemark2file filemark2file : filemarkVo.getFilemark2fileList()) {
            filemark2file.setId(UUID.randomUUID().toString());
            filemark2file.preInsert(tokenModel);
            filemark2fileMapper.insert(filemark2file);
        }
        return ApiResult.success();
    }
}
