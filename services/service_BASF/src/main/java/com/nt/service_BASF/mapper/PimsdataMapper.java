package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Pimsdata;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public interface PimsdataMapper extends MyMapper<Pimsdata> {
    List<Pimsdata> getAllPimsInfo(@Param("type") String type) throws Exception;

    int insertPimsDataList(List<Pimsdata> pimsdataList) throws Exception;

    List<Pimsdata> getLastestPims() throws Exception;
}
