package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Operationdetail;
import com.nt.dao_PHINE.Vo.OperationRecordVo;
import com.nt.service_PHINE.OperationrecordService;
import com.nt.service_PHINE.PHINEFileService;
import com.nt.service_PHINE.mapper.FileinfoMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class PHINEFileServiceImpl implements PHINEFileService {

    @Autowired
    private FileinfoMapper fileinfoMapper;
    @Autowired
    private OperationrecordService operationrecordService;

    @Override
    public ApiResult saveFileInfo(TokenModel tokenModel, List<Fileinfo> filesInfo, String projectId) throws Exception {
        // 操作记录表详情集合
        List<Operationdetail> operationdetails = new ArrayList<Operationdetail>();
        filesInfo.forEach(item -> {
            item.setId(UUID.randomUUID().toString());
            item.preInsert(tokenModel);
            // 操作记录详情
            Operationdetail operationdetail = new Operationdetail();
            operationdetail.setDevicename(item.getDeviceid());
            operationdetail.setConfigurationtype(item.getFiletype());
            operationdetail.setFilename(item.getFilename());
            operationdetails.add(operationdetail);
        });
        int result = fileinfoMapper.saveFilesInfo(filesInfo);
        if (result > 0) {
            // 生成操作记录
            OperationRecordVo operationRecordVo = new OperationRecordVo();
            operationRecordVo.setTitle("文件上传");
            operationRecordVo.setContent("上传" + result + "个文件");
            operationRecordVo.setDetailist(operationdetails);
            operationRecordVo.setProjectid(projectId);
            operationrecordService.addOperationrecord(operationRecordVo);
            return ApiResult.success(MsgConstants.INFO_01);
        } else {
            return ApiResult.fail("保存文件信息" + MsgConstants.ERROR_01);
        }
    }

    @Override
    public ApiResult getFileByProjectId(String projectId) throws Exception {
        Fileinfo fileinfo = new Fileinfo();
        fileinfo.setId(projectId);
        List<Fileinfo> fileinfoList = fileinfoMapper.select(fileinfo);
        return ApiResult.success(MsgConstants.INFO_01, fileinfoList);
    }
}
