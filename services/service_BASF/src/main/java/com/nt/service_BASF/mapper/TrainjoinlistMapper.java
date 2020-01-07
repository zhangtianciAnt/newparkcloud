package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Emergencytemplate;
import com.nt.dao_BASF.Trainjoinlist;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: TrainjoinlistMapper
 * @Author: 王哲
 * @Description: 培训参加名单Mapper
 * @Date: 2020/1/7 11:10
 * @Version: 1.0
 */
@Component(value = "TrainjoinlistMapper")
public interface TrainjoinlistMapper extends MyMapper<Trainjoinlist> {
}
