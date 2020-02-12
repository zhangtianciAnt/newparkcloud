package com.nt.service_PHINE.mapper;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Filemark;
import com.nt.dao_PHINE.Vo.FilemarkCheckVo;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FilemarkMapper extends MyMapper<Filemark> {
    List<Filemark> getFileMarkByProjectId(String projectId);

    // 文件标记Check
    String checkExists(FilemarkCheckVo filemarkCheckVo);
}
