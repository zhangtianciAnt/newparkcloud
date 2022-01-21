package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.utils.MyMapper;

import java.util.List;

public interface NapalmMapper extends MyMapper<Napalm> {

//    添加筛选条件 ztc fr
    List<Napalm> selectList(Napalm napalm);
//    添加筛选条件 ztc to
}
