package com.nt.service_PHINE;

import com.nt.dao_PHINE.Vo.OperationRecordVo;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE
 * @ClassName: OperationrecordService
 * @Description: 操作记录Service接口
 * @Author: SKAIXX
 * @CreateDate: 2020/2/3
 * @Version: 1.0
 */
public interface OperationrecordService {

    // 项目管理画面获取操作记录列表
    List<OperationRecordVo> getOperationrecordList(String projectId);

    //创建操作记录
    void addOperationrecord(OperationRecordVo operation);
}
