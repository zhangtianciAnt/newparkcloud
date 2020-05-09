package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.CompanyStatistics;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.Vo.bpSum3Vo;
import com.nt.dao_Pfans.PFANS6000.Vo.bpSum2Vo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyStatisticsMapper extends MyMapper<CompanyStatistics> {

    List<CompanyStatistics> getWorkTimes(Map<String, Object> coststatistics);

    List<CompanyStatistics> getWorkers(Map<String, Object> coststatistics);

    List<bpSum3Vo> getbpsum(@Param("groupid") String groupid,@Param("years") String years);

    List<bpSum2Vo> getbpsum2(@Param("groupid") String groupid,@Param("years") String years);

}

