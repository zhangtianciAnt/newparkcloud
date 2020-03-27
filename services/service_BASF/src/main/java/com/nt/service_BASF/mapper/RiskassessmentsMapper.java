package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Riskassessments;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
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

    //根据装置code查找今日有无填写信息
    int checkExist(@Param("code") String devicecode) throws Exception;
}
