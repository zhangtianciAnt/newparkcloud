package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Communication;
import com.nt.utils.MyMapper;

import java.util.List;

public interface CommunicationMapper extends MyMapper<Communication> {
    List<Communication> selectCommunication();
}
