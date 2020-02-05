package com.nt.service_PHINE.mapper;

import com.nt.dao_PHINE.Operationrecord;
import com.nt.dao_PHINE.Vo.OperationRecordVo;
import com.nt.utils.MyMapper;

import java.util.List;

public interface OperationrecordMapper extends MyMapper<Operationrecord> {
    //操作记录信息列表（包含操作详细信息）
    List<OperationRecordVo> getOperationrecordList(String projectId);
}
