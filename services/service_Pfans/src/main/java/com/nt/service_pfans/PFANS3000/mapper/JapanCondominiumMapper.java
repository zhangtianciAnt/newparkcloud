package com.nt.service_pfans.PFANS3000.mapper;

import com.nt.dao_Pfans.PFANS3000.JapanCondominium;
import com.nt.utils.MyMapper;
import java.util.List;

public interface JapanCondominiumMapper extends MyMapper<JapanCondominium> {
    List<JapanCondominium> getJapanCondominium();
}