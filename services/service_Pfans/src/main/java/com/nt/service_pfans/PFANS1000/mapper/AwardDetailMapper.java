package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.AwardDetail;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AwardDetailMapper extends MyMapper<AwardDetail> {
    //PSDCD_PFANS_20210723_XQ_086 委托决裁报销明细自动带出 ztc
    List<AwardDetail> getAdInfoList(@Param("awardIdList") List<String> awardIdList);
}
