package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Emergencytemplate;
import com.nt.dao_BASF.Startprogram;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: StartprogramMapper
 * @Author: 王哲
 * @Description: 申请考核Mapper
 * @Date: 2020/1/7 11:08
 * @Version: 1.0
 */
@Component(value = "StartprogramMapper")
public interface StartprogramMapper extends MyMapper<Startprogram> {
}
