package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Filemark;
import com.nt.dao_PHINE.Filemark2file;
import com.nt.dao_PHINE.Operationdetail;
import com.nt.dao_PHINE.Vo.OperationRecordVo;
import com.nt.service_PHINE.OperationrecordService;
import com.nt.service_PHINE.PHINEFileService;
import com.nt.service_PHINE.mapper.FileinfoMapper;
import com.nt.service_PHINE.mapper.Filemark2fileMapper;
import com.nt.service_PHINE.mapper.FilemarkMapper;
import com.nt.utils.ApiResult;
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
    @Autowired
    private FilemarkMapper filemarkMapper;
    @Autowired
    private Filemark2fileMapper filemark2fileMapper;

    @Override
    public ApiResult saveFileInfo(TokenModel tokenModel, List<Fileinfo> filesInfo, String projectId) throws Exception {
        // 操作记录表详情集合
        List<Operationdetail> operationdetails = new ArrayList<Operationdetail>();

        for (Fileinfo fileinfo : filesInfo) {
            Fileinfo tmp = new Fileinfo();
            // 判断文件名称是否已经存在
            tmp.setFilename(fileinfo.getFilename());
            tmp = fileinfoMapper.selectOne(tmp);
            if ( tmp != null) {     // 如果文件已存在，则删除原文件信息
                fileinfoMapper.deleteByPrimaryKey(tmp);
            }
            // 判断用户是否选择了"ALL"
            if (fileinfo.getFpgaid().equals("ALL")) {
                int index = 1;
                while (index <= 6) {
                    fileinfo.setFileid(UUID.randomUUID().toString());
                    fileinfo.setFpgaid(String.valueOf(index));  // Fpga1~6
                    fileinfo.preInsert(tokenModel);
                    fileinfoMapper.insert(fileinfo);
                    index ++;
                }
            } else {
                fileinfo.setFileid(UUID.randomUUID().toString());
                fileinfo.preInsert(tokenModel);
                fileinfoMapper.insert(fileinfo);
            }

            // 操作记录详情
            Operationdetail operationdetail = new Operationdetail();
            operationdetail.setDevicename(fileinfo.getDeviceid());
            operationdetail.setConfigurationtype(fileinfo.getFiletype());
            operationdetail.setFilename(fileinfo.getFilename());
            operationdetail.setFileid(fileinfo.getFileid());
            operationdetails.add(operationdetail);
        }
        // 生成操作记录
        OperationRecordVo operationRecordVo = new OperationRecordVo();
        operationRecordVo.setTitle("文件上传");
        operationRecordVo.setContent("上传" + filesInfo.size() + "个文件");
        operationRecordVo.setDetailist(operationdetails);
        operationRecordVo.setProjectid(projectId);
        operationrecordService.addOperationrecord(tokenModel, operationRecordVo);
        return ApiResult.success(MsgConstants.INFO_01);
    }

    @Override
    public ApiResult getFileByProjectId(String projectId) throws Exception {
        Fileinfo fileinfo = new Fileinfo();
        fileinfo.setProjectid(projectId);
        List<Fileinfo> fileinfoList = fileinfoMapper.select(fileinfo);
        return ApiResult.success(MsgConstants.INFO_01, fileinfoList);
    }

    @Override
    public ApiResult getFileMarkByProjectId(String projectId) throws Exception {
        List<Filemark> filemarkList = filemarkMapper.getFileMarkByProjectId(projectId);
        return ApiResult.success(filemarkList);
    }

    @Override
    public ApiResult getFilesByFileMarkId(String projectId) throws Exception {
        List<Fileinfo> fileinfoList = fileinfoMapper.getFilesByFileMarkId(projectId);
        return ApiResult.success(fileinfoList);
    }

    @Override
    public ApiResult updateFileNameById(TokenModel tokenModel, Fileinfo fileinfo) throws Exception {
        fileinfo.preUpdate(tokenModel);
        int result = fileinfoMapper.updateByPrimaryKeySelective(fileinfo);
        if (result > 0) {
            return ApiResult.success(MsgConstants.INFO_01);
        } else {
            return ApiResult.fail("更新文件信息" + MsgConstants.ERROR_01);
        }
    }

    @Override
    public ApiResult delFileMark2File(List<Filemark2file> filemark2fileList) throws Exception {
        filemark2fileList.forEach(item -> {
            filemark2fileMapper.delete(item);
        });
        return ApiResult.success(MsgConstants.INFO_01);
    }

    /**
     * @return
     * @Method getLogicLoadHistory
     * @Author SKAIXX
     * @Description 获取前回逻辑加载的文件列表
     * @Date 2020/2/21 8:34
     * @Param
     **/
    @Override
    public ApiResult getLogicLoadHistory(String projectId) throws Exception {
        // 通过ProjectId获取操作记录
        List<OperationRecordVo> operationRecordVoList = operationrecordService.getOperationrecordList(projectId);
        List<Fileinfo> fileinfoList = new ArrayList<>();
        for (OperationRecordVo item : operationRecordVoList) {
            // 如果存在逻辑加载的操作记录
            if (item.getTitle().equals("逻辑加载")) {
                // 根据操作记录获取文件列表
                item.getDetailist().forEach(subItem -> {
                    Fileinfo fileinfo = fileinfoMapper.selectByPrimaryKey(subItem.getFileid());
                    fileinfo.setRemarks(subItem.getOperationresult());
                    fileinfoList.add(fileinfo);
                });
                break;
            }
        }
        return ApiResult.success(fileinfoList);
    }

    /**
     * @return
     * @Method isExistSameNameFile
     * @Author SKAIXX
     * @Description 判断文件服务器中是否存在同名文件
     * @Date 2020/2/21 21:58
     * @Param
     **/
    @Override
    public ApiResult isExistSameNameFile(List<Fileinfo> fileinfoList) throws Exception {
        for (Fileinfo fileinfo : fileinfoList) {
            Fileinfo tmp = new Fileinfo();
            tmp.setFilename(fileinfo.getFilename());
            int fileCnt = fileinfoMapper.selectCount(tmp);
            fileinfo.setRemarks(fileCnt > 0 ? "1" : "0");
        }
        return ApiResult.success(fileinfoList);
    }
}
