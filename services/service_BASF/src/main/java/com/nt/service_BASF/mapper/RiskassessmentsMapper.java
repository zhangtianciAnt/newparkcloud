package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Riskassessments;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: RiskassessmentsMapper
 * @Author: 王哲
 * @Description: 风险研判（MySql表）Mapper
 * @Date: 2020/3/25 15:32
 * @Version: 1.0
 */
@Component(value = "RiskassessmentsMapper")
public interface RiskassessmentsMapper extends MyMapper<Riskassessments> {


}
