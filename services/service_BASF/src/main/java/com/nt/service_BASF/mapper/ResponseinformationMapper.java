package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Record;
import com.nt.dao_BASF.Responseinformation;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: ResponseinformationMapper
 * @Author: 王哲
 * @Description: 应急预案响应信息实现类
 * @Date: 2019/12/19 16:34
 * @Version: 1.0
 */
@Component(value = "ResponseinformationMapper")
public interface ResponseinformationMapper extends MyMapper<Responseinformation> {
}
