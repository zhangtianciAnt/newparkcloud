package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Record;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: RecordMapper
 * @Author: Newtouch
 * @Description: 人员培训记录Mapper
 * @Date: 2019/11/25 13:50
 * @Version: 1.0
 */
@Component(value = "RecordMapper")
public interface RecordMapper extends MyMapper<Record> {
}
