package com.nt.service_PHINE.mapper;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Vo.OperationRecordVo;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FileinfoMapper extends MyMapper<Fileinfo> {
    int saveFilesInfo(List<Fileinfo> filesInfo);
}