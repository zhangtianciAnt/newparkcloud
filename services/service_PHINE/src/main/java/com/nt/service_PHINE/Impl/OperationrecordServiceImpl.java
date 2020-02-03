package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Operationrecord;
import com.nt.service_PHINE.OperationrecordService;
import com.nt.service_PHINE.mapper.OperationrecordMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE.Impl
 * @ClassName: OperationrecordServiceImpl
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2020/2/3
 * @Version: 1.0
 */
public class OperationrecordServiceImpl implements OperationrecordService {

    @Autowired
    private OperationrecordMapper operationrecordMapper;

    /**
     * @return
     * @Method getOperationrecordList
     * @Author SKAIXX
     * @Description 项目管理画面获取操作记录列表
     * @Date 2020/2/3 16:51
     * @Param
     **/
    @Override
    public List<Operationrecord> getOperationrecordList(String projectId) {
        Operationrecord operationrecord = new Operationrecord();
        operationrecord.setProjectid(projectId);
        return operationrecordMapper.select(operationrecord);
    }
}
