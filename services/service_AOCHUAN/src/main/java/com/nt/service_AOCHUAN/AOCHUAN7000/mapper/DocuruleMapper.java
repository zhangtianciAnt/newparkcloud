package com.nt.service_AOCHUAN.AOCHUAN7000.mapper;


import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocuruleMapper extends MyMapper<Docurule> {

    Docurule selectByDocutype(@Param("docutype") String docutype);

}
