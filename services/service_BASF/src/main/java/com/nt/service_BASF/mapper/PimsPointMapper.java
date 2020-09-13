package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Pimspoint;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public interface PimsPointMapper extends MyMapper<Pimspoint> {
    List<Pimspoint> getPimsPoint(@Param("pimspoints") List<Pimspoint> pimspoints, @Param("pimspointMap") Map<String, String> pimspointMap);
}
