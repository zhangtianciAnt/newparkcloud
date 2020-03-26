package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Unusedevice;
import com.nt.utils.MyMapper;

import java.util.List;

public interface UnusedeviceMapper extends MyMapper<Unusedevice> {

    List<Unusedevice> selectUnusedevice();
}
