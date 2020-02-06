package com.nt.service_PHINE.mapper;

import com.nt.dao_PHINE.Operationdetail;
import com.nt.dao_PHINE.Operationrecord;
import com.nt.dao_PHINE.Vo.OperationRecordVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OperationrecordMapper extends MyMapper<Operationrecord> {
    //操作记录信息列表（包含操作详细信息）
    List<OperationRecordVo> getOperationrecordList(String projectId);

    //批量创建操作详情信息
    void insertdetaillist(@Param("detaillist")List<Operationdetail> detaillist) ;
}
