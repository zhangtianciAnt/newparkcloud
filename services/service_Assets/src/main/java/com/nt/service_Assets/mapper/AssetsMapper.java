package com.nt.service_Assets.mapper;

import com.nt.dao_Assets.Assets;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface AssetsMapper extends MyMapper<Assets> {

    String getMaxCode(@Param("type") String Asstype,@Param("type") String Bartype) throws Exception;
}
