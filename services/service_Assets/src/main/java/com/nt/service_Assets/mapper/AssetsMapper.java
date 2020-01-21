package com.nt.service_Assets.mapper;

import com.nt.dao_Assets.Assets;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface AssetsMapper extends MyMapper<Assets> {

    String getMaxCode(@Param("Asstype") String Asstype,@Param("Bartype") String Bartype);
}
