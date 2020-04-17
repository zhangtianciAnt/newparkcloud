package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.VO.TrainingRecordsExportVo;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: TrainingRecordsExport
 * @Author: 王哲
 * @Description: 培训档案导出
 * @Date: 2020/4/17 15:45
 * @Version: 1.0
 */
@Component(value = "TrainingRecordsExportMapper")
public interface TrainingRecordsExportMapper extends MyMapper<TrainingRecordsExportVo> {
    List<TrainingRecordsExportVo> exportRecordData() throws Exception;
}
